package task.Unit;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUnit {
	private Date DateTime;
	private int year;
	private int month;
	private int day;
	private int hours;
	private int mins;
	private int seconds;
	private SimpleDateFormat  dateFormat;
	public DateUnit() {
	
	}
	public DateUnit(Date unit) {
		DateTime = unit;
		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = dateFormat.format(unit);
		String str[] = date.split(" ");
		String ymd[] = str[0].split("-");
		String hms[] = str[1].split(":");
		year = Integer.valueOf(ymd[0]);
		month = Integer.valueOf(ymd[1]);
		day = Integer.valueOf(ymd[2]);
		hours = Integer.valueOf(hms[0]);
		mins = Integer.valueOf(hms[1]);
		seconds = Integer.valueOf(hms[2]);
		//System.out.println(year+"-"+month+"-"+day+" "+hours+":"+mins+":"+seconds);
		
	}
	/* get String date 
	 * */
	public String getDate() {
		return dateFormat.format(DateTime);
	}
	public Date getDate(String unit) {
		try {
		return dateFormat.parse(unit);
		}catch(Exception e) {
			e.printStackTrace();
			return new Date();
		}
	}
	//type  0(今天之内) 1(一周之内) 2(一月之内) 3(今年内) 4(过去三年) 5(全部日期)
	public String getStart(int type) {
		if(type == 0) {
			return getTodayStart();
		}
		else if(type == 1) {
			return getWeekStart();
		}
		else if(type == 2) {
			return getMonthStart();
		}
		else if( type == 3) {
			return getYearStart();
		}
		else if(type == 4) {
			return getThrYearStart();
		}
		else {
			return getLongLongAgo();
		}
	}
	//type  0(今天之内) 1(一周之内) 2(一月之内) 3(今年内) 4(过去三年) 5(全部日期)
	public String getEnd(int type) {
		if(type == 0) {
			return getTodayEnd();
		}
		else if(type == 1) {
			return getTodayEnd();
		}
		else if(type == 2) {
			return getMonthEnd();
		}
		else {
			return getYearEnd();
		}
	}
	/* return a future date after the Date
	 * */
	public String getTodayStart() {
		String todayStart = new String(year+"-"+month+"-"+day+ " 00:00:00");
		return todayStart;
	}
	public Date getTodatStartTime() {
		try {
				return dateFormat.parse(getTodayStart());
			}
			catch (Exception e) {
				e.printStackTrace();
				return new Date();
			}
	}
	public String getTodayEnd() {
		String todayEnd = new String(year+"-"+month+"-"+day+ " 23:59:59");
		return todayEnd;
	}
	public Date getTodatEnding() {
		try {
			return dateFormat.parse(getTodayEnd());
		}
		catch (Exception e) {
			e.printStackTrace();
			return new Date();
		}
	}
	public String getWeekStart() {
		int day = this.day, year = this.year,month = this.month;
		day -= 7;
		if(day <= 0) {
			int smallMonth[] = {2,4,6,9,11};
			int i = 0;
			for( ; i<smallMonth.length; i++)
				if(smallMonth[i] == this.month) break;
			if(i == smallMonth.length)  {
				day += 30;
				}
			else {
				day += 31;
			}
			month --;
			if(month == 0) {
				month = 12;
				year--;
			}
		}
		String oneWeek =new String(year+ "-"+month+"-"+day+ " 00:00:00");
		return oneWeek;
	}
	public String getMonthStart() {
		String monthStart = new String(year+ "-"+month+"-1 00:00:00");
		return monthStart;
	}
	public String getMonthEnd() {
		String monthEnd = new String(year+ "-"+(month+1)+"-1 00:00:00");
		return monthEnd;
	}
	public String getYearStart() {
		String yearStart = new String(year+ "-1-1 00:00:00");
		return yearStart;
	}
	public String getYearEnd() {
		String yearEnd = new String(year+ "-12-31 23:59:59");
		return yearEnd;
	}
	public String getThrYearStart() {
		String yearStart = new String((year-3)+ "-1-1 00:00:00");
		return yearStart;
	}
	public String getLongLongAgo() {
		String yearStart = new String((year-100)+ "-1-1 00:00:00");
		return yearStart;
	}
	public String getMinsFuture(int ms) {
		mins += ms;
		if(mins >= 60) {
			mins -= 60;
			hours++;
			if(hours >= 24) {
				hours -= 24;
				day ++;
				int smallMonth[] = {2,4,6,9,11};
				int i = 0;
				for( ; i<smallMonth.length; i++)
					if(smallMonth[i] == this.month) break;
				if(i != smallMonth.length) { // 当前月有30天
					if(month == 2 && day > 28)
					day -= 28; //前一个月有31天
					else if(day > 30){
						day -= 30;
					}
					month++;
					}
				else {
					if(day > 31) {
						day -= 31;
						month++;
						if(month > 12) {
							month -= 12;
							year++;
						}
					}
				}
			}
		}
		return new String(year+"-"+month+"-"+day+" "+hours+":"+mins+":"+seconds);
	}
	public Date getMinFuture() {
		String minsFuture = getMinsFuture(5);
		System.out.println(minsFuture);
		try {
			return dateFormat.parse(minsFuture);
			}catch(Exception e) {
				e.printStackTrace();
				return new Date();
			}
	}
	public String getMinsAgo() {
		mins -= 2;
		if(mins < 0) {
			mins += 60;
			hours--;
			if(hours < 0) {
				hours += 24;
				day --;
				int smallMonth[] = {2,4,6,9,11};
				int i = 0;
				for( ; i<smallMonth.length; i++)
					if(smallMonth[i] == this.month) break;
				if(i != smallMonth.length) { // 当前月有31天 前一个月有30天
					/// 1 3 5 7 8  10 12
					if(day < 0) {
						if(month == 8 || month == 1) {
							day += 31;
						}
						else if (month == 3) {
							day += 28;
						}
						else day += 30;
					}
				}
				else {
						if(day < 0) {
						 day += 31;
						 month--;
					   }
				}
				if(month < 0) year--;
			}
		}
		return new String(year+"-"+month+"-"+day+" "+hours+":"+mins+":"+seconds);
}
	public Date doDate( int y, int m, int d) {
		int smallMonth[] = {2,4,6,9,11};
		if( d != 0 ) {
			this.day += d;
			if(this.day >= 30) {
				int i = 0;
				for( ; i<smallMonth.length; i++)
					if(smallMonth[i] == this.month) break;
				if(i != smallMonth.length) {
					this.day -= 30;
					month++;
				}
				else if(this.day >= 31) {
					this.day -= 31;
					month++;
				}
			}
		}
		this.month += m;
		if(this.month > 12) {
			this.month -= 12;
			year ++;
		}
		this.year += y;
		
		String test = new String(year+"-"+month+"-"+day+" "+hours+":"+mins+":"+seconds);
		try {
		return dateFormat.parse(test);
		}catch (Exception e) {
			e.printStackTrace();
			return new Date();
		}
	}
	
}
