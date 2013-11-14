Date.prototype.getDayOfYear = function() {
	var jan_first = new Date(this.getFullYear(),0,1);
	return Math.ceil((this - jan_first) / 86400000);
};

Date.prototype.getUTCDayOfYear = function() {
	var local_jan_first = new Date(this.getFullYear(),0,1);
	var tz_offset = local_jan_first.getTimezoneOffset();
	tz_offset = ( tz_offset * 60 * 1000 );
	var tz_adjusted_jan_first = new Date(local_jan_first - tz_offset);
	return Math.ceil((this - tz_adjusted_jan_first) / 86400000);
};

Date.prototype.toDayMonthFullYear = function() {
	var idx = this.getMonth();
	var months = ["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"];
	var month = months[idx];
	return this.getDate() + " " + month + " " + this.getFullYear();
};

Date.prototype.toUTCDayMonthFullYear = function() {
	var idx = this.getUTCMonth();
	var months = ["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"];
	var month = months[idx];
	return  this.getUTCDate() + " " + month + " " + this.getUTCFullYear();
};

Date.prototype.getUTCSecondsFromMidnight = function() {
	return (this.getUTCHours() * 3600) + (this.getUTCMinutes() * 60) + this.getUTCSeconds();
};

function pad2(number) {
	return (number < 10 ? '0' : '') + number
}

function epochToDateFields(epoch) {
	if (!epoch) return {
		mmddyyyy: '',
		hh: '00',
		mm: '00',
		ss: '00'
	};
	var d = new Date(epoch);
	return {
//		m: d.getUTCMonth() + 1,
//		d: d.getUTCDate(),
//		yyyy: d.getUTCFullYear(),
		mmddyyyy: pad2(d.getUTCMonth() + 1) + "/" + pad2(d.getUTCDate()) + "/" + d.getUTCFullYear(),
		hh: pad2(d.getUTCHours()),
		mm: pad2(d.getUTCMinutes()),
		ss: pad2(d.getUTCSeconds())
	};
}

function dateFieldsToEpoch(mmddyyyy,hh,mm) {
	var df = mmddyyyy.split('/'),
		d = new Date(Date.UTC(df[2],df[0]-1,df[1],hh,mm,0));
	return d.getTime();
}

function epochToDateTime(epoch) {
	if (!epoch) return "";
	var d = new Date(epoch);
	var months = ["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"];
	return months[d.getUTCMonth()] + " "+ d.getUTCDate() + " " + d.getUTCFullYear() + " " + pad2(d.getUTCHours()) + ":" + pad2(d.getUTCMinutes()) + ":" + pad2(d.getUTCSeconds());
}

function epochToAxisTime(epoch, include_secs) {
	if (!epoch) return "";
	var d = new Date(epoch);
	var months = ["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"];
	return months[d.getUTCMonth()] + " "+ d.getUTCDate() + " " + pad2(d.getUTCHours()) + ":" + pad2(d.getUTCMinutes()) + (include_secs ? ":"+pad2(d.getUTCSeconds()) : '');
}

function msToHuman(ms) {
	if (typeof ms === 'undefined' || isNaN(ms)) return "n/a";
	var seconds = Math.floor(ms / 1000);
	var numyears = Math.floor(seconds / 31536000);
	var numdays = Math.floor((seconds % 31536000) / 86400);
	var numhours = Math.floor(((seconds % 31536000) % 86400) / 3600);
	var numminutes = Math.floor((((seconds % 31536000) % 86400) % 3600) / 60);
	var numseconds = (((seconds % 31536000) % 86400) % 3600) % 60;
	if (numyears || numdays > 60) return "(no data)";
	if (numdays)
		return numdays + " days " + numhours + " hours ago";
	if (numhours)
		return numhours + " hours " + numminutes + " min ago";
	if (numminutes)
		return numminutes + " min " + numseconds + " sec ago";
	return numseconds + " seconds ago";
}
