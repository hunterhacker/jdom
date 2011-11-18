/*--

 Copyright (C) 2000-2004 Jason Hunter & Brett McLaughlin.
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:

 1. Redistributions of source code must retain the above copyright
    notice, this list of conditions, and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions, and the disclaimer that follows
    these conditions in the documentation and/or other materials
    provided with the distribution.

 3. The name "JDOM" must not be used to endorse or promote products
    derived from this software without prior written permission.  For
    written permission, please contact <request_AT_jdom_DOT_org>.

 4. Products derived from this software may not be called "JDOM", nor
    may "JDOM" appear in their name, without prior written permission
    from the JDOM Project Management <request_AT_jdom_DOT_org>.

 In addition, we request (but do not require) that you include in the
 end-user documentation provided with the redistribution and/or in the
 software itself an acknowledgement equivalent to the following:
     "This product includes software developed by the
      JDOM Project (http://www.jdom.org/)."
 Alternatively, the acknowledgment may be graphical using the logos
 available at http://www.jdom.org/images/logos.

 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED.  IN NO EVENT SHALL THE JDOM AUTHORS OR THE PROJECT
 CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 SUCH DAMAGE.

 This software consists of voluntary contributions made by many
 individuals on behalf of the JDOM Project and was originally
 created by Jason Hunter <jhunter_AT_jdom_DOT_org> and
 Brett McLaughlin <brett_AT_jdom_DOT_org>.  For more information
 on the JDOM Project, please see <http://www.jdom.org/>.

 */

// Based on code Copyright (c) 1998-2000 Alex Chaffee and Purple Technology.

package org.jdom2.contrib.beans;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.text.*;

/**
 * @author Alex Chaffee (alex@jguru.com)
 **/
@SuppressWarnings("javadoc")
public class DateUtils {

	public static boolean debug;

	/**
	 * Tries to parse the date according to several different formats.
	 * <p>
	 * BUG in TimeZone processing -- Calendar class always screws up the time when a TZ is set -- so ignored for now.
	 * 
	 * @return null if not parseable
	 **/
	@SuppressWarnings("deprecation")
	public static Date parseDate(String s) {

		// some standard date format
		try {
			// this is deprecated, but it still parses more
			// formats than DateFormat.parse(String)
			return new Date(s);
		}
		catch (IllegalArgumentException dfe) {
			// do nothing... we try to recover.
		}

		// some other (?) standard date format
		try {
			return DateFormat.getDateInstance().parse(s);
		}
		catch (ParseException pe) {
			// OK, this is challenging, but we try the next option.
		}

		// a single int = msec since 1970
		try {
			return new Date(Long.parseLong(s));
		}
		catch (NumberFormatException nfe) {
			// Getting rediculous now...
		}

		ISO8601 iso = parseISO8601(s);
		if (iso != null) {
			Calendar cal = Calendar.getInstance();

			cal.set(Calendar.YEAR, iso.year);			
			cal.set(Calendar.MONTH, iso.month - 1);
			cal.set(Calendar.DAY_OF_MONTH, iso.day);
			cal.set(Calendar.HOUR, iso.hour + 12);	// ??? TZ bug again?
			cal.set(Calendar.MINUTE, iso.min);
			cal.set(Calendar.SECOND, iso.sec);		

			return cal.getTime();	// why the hell does getTime() return a Date?    


		} // if iso

		return null;
	} // parseDate

	public static class ISO8601 {
		public int year;
		public int month;
		public int day;
		public int hour;
		public int min;
		public int sec;
		public int frac;
		public String tz;
	}

	protected static String reISO8601 =
			"(\\d\\d\\d\\d)(-(\\d\\d)(-(\\d\\d))?)?" +
					"([T| ]?" +
					"(\\d\\d):(\\d\\d)(:((\\d\\d)(\\.(\\d+))?)?)?" +
					"(Z|([+-]\\d\\d:\\d\\d)|([A-Z]{3}))?)?";

	public static ISO8601 parseISO8601(String s) {
		// ISO 8601 datetime: http://www.w3.org/TR/NOTE-datetime
		// e.g. 1997-07-16T19:20:30.45+01:00
		// additions: "T" can be a space, TZ can be a three-char code, TZ can be missing
		try {
			Pattern pat = Pattern.compile(reISO8601);
			Matcher re = pat.matcher(s);
			if (re.matches()) {
				if (debug)
					showParens(re);

				ISO8601 iso = new ISO8601();
				iso.year = toInt(re.group(1));
				iso.month = toInt(re.group(3));
				iso.day = toInt(re.group(5));
				iso.hour = toInt(re.group(7));
				iso.min = toInt(re.group(8));
				iso.sec = toInt(re.group(11));
				iso.frac = toInt(re.group(13));
				iso.tz = re.group(14);

				if (debug) {
					System.out.println("year='" + iso.year + "'");
					System.out.println("month='" + iso.month + "'");
					System.out.println("day='" + iso.day + "'");
					System.out.println("hour='" + iso.hour + "'");
					System.out.println("min='" + iso.min + "'");
					System.out.println("sec='" + iso.sec + "'");
					System.out.println("frac='" + iso.frac + "'");
					System.out.println("tz='" + iso.tz + "'");
				}

				return iso;
			}
		} // try
		catch (PatternSyntaxException ree) {
			ree.printStackTrace();
		}
		return null;
	}

	public static int toInt(String x) {
		if (x == null) return 0;
		try {
			return Integer.parseInt(x);
		}
		catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * Dump parenthesized subexpressions found by a regular expression matcher object
	 * @param r Matcher object with results to show
	 */
	static void showParens(Matcher r)
	{
		// Loop through each paren
		for (int i = 0; i < r.groupCount(); i++)
		{
			// Show paren register
			System.out.println("$" + i + " = " + r.group(i));
		}
	}

	public static void main(String[] args) {
		debug = true;
		for (int i=0; i<args.length; ++i) {
			System.out.println( parseDate(args[i]) );
		}
	}
}
