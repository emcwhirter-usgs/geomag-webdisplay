DROP PROCEDURE IF EXISTS  bin_change_detector;
DELIMITER //

CREATE PROCEDURE bin_change_detector (  
    IN start_epoch BIGINT,  
    IN end_epoch   BIGINT,
    IN station     VARCHAR(255)
)

READS SQL DATA
BEGIN

  /*-- All 'DECLARE' statements must come first */

  /*-- Declare '_val' variables to read in each record from the cursor*/
  
  DECLARE id_pre_val            BIGINT(20) DEFAULT 0;
  DECLARE epoch_pre_val         BIGINT(20) DEFAULT 0;
  DECLARE timestamp_pre_val     DATETIME;
  DECLARE bh_pre_val            INT(11) DEFAULT 0;
  DECLARE be_pre_val            INT(11) DEFAULT 0;
  DECLARE bz_pre_val            INT(11) DEFAULT 0;
  DECLARE station_pre_val       VARCHAR(255) DEFAULT 0;

  DECLARE id_val                BIGINT(20);
  DECLARE epoch_val             BIGINT(20);
  DECLARE timestamp_val         DATETIME;
  DECLARE bh_val                INT(11);
  DECLARE be_val                INT(11);
  DECLARE bz_val                INT(11);
  DECLARE station_val           VARCHAR(255);

  -- Declare variables used just for cursor and loop control
  DECLARE no_more_rows          BOOLEAN;
  DECLARE loop_cntr             INT DEFAULT 0;
  DECLARE num_rows              INT DEFAULT 0;

  -- Declare the cursor
  DECLARE raw_flux_gate_cur CURSOR FOR
        SELECT id,epoch,timestamp,bh,be,bz,station 
        FROM   raw_flux_gate_msg 
        WHERE  epoch > start_epoch AND epoch <= end_epoch
        ORDER BY epoch;

  -- Declare 'handlers' for exceptions
  DECLARE CONTINUE HANDLER FOR NOT FOUND
  SET no_more_rows = TRUE;

  -- Now the programming logic 

  -- 'open' the cursor and capture the number of rows returned
  -- (the 'select' gets invoked when the cursor is 'opened')

  OPEN raw_flux_gate_cur;
  select FOUND_ROWS() into num_rows;

  the_loop: LOOP

    IF loop_cntr = 0 THEN
        FETCH  raw_flux_gate_cur
        INTO   id_pre_val,epoch_pre_val,timestamp_pre_val,bh_pre_val,be_pre_val,bz_pre_val,station_pre_val;
    ELSE
        FETCH  raw_flux_gate_cur
        INTO id_val,epoch_val,timestamp_val,bh_val,be_val,bz_val,station_val;

        IF (bh_val != bh_pre_val OR be_val != be_pre_val OR bz_val != bz_pre_val ) THEN

           /* 
             Uncomment the following select statements for 
             the equivalent of a 'print statement' in a stored procedure.
             Simply displays output for each loop if any bin values are different
           */

          -- select id_pre_val,epoch_pre_val,bh_pre_val,be_pre_val,bz_pre_val,station_pre_val;
          -- select id_val,epoch_val,bh_val,be_val,bz_val,station_val;

	   INSERT bin_log(before_id,after_id,station,timestamp) values(id_pre_val,id_val,station_val,timestamp_val);

        END IF;

        SET id_pre_val = id_val;
        SET epoch_pre_val = epoch_val;
        SET timestamp_pre_val = timestamp_val;
        SET bh_pre_val = bh_val;
        SET be_pre_val = be_val;
        SET bz_pre_val = bz_val;
        SET station_pre_val = station_val;

    END IF;

   -- break out of the loop if
   -- 1) there were no records, or
   -- 2) we've processed them all

    IF no_more_rows THEN
        CLOSE raw_flux_gate_cur;
        LEAVE the_loop;
    END IF;

    -- count the number of times looped
    SET loop_cntr = loop_cntr + 1;

  END LOOP the_loop;

  -- 'print' the output so we can see they are the same
  -- select num_rows, loop_cntr;

END
//
DELIMITER ;
