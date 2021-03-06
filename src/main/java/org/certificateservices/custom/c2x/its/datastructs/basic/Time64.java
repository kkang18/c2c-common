/************************************************************************
*                                                                       *
*  Certificate Service -  Car2Car Core                                  *
*                                                                       *
*  This software is free software; you can redistribute it and/or       *
*  modify it under the terms of the GNU Affero General Public License   *
*  License as published by the Free Software Foundation; either         *
*  version 3   of the License, or any later version.                    *
*                                                                       *
*  See terms of license at gnu.org.                                     *
*                                                                       *
*************************************************************************/
package org.certificateservices.custom.c2x.its.datastructs.basic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import org.certificateservices.custom.c2x.common.Encodable;
import org.certificateservices.custom.c2x.its.datastructs.cert.Certificate;

import net.time4j.Moment;
import net.time4j.TemporalType;
import net.time4j.scale.TimeScale;


/**
 * For Version 2 certificates:
 * <p>
 * Time64 is a 64-bit unsigned integer, encoded in big-endian format, giving the number of International Atomic Time
 * (TAI) microseconds since 00:00:00 UTC, 01 January 2004. 
 * <p>
 * For Version 1 certificates:
 * <p>
 * Time64 is an unsigned 64-bit integer, encoded in big-endian format, giving the number of International Atomic Time
 * (TAI) microseconds since 00:00:00 UTC, 1 January, 2010.
 *
 * 
 * @author Philip Vendil, p.vendil@cgi.com
 *
 */
public class Time64 implements Encodable{
	
	private static final long SECONDSBETWEENTAIZEROAND2004 = 1009843232L;
	private static final long SECONDSBETWEENTAIZEROAND2010 = 1199232034L;
	
	private BigInteger timeStamp;
	
	/**
	 * Main constructor for Time64
	 * 
	 * <b>Important: Note that this transformation is sometimes lossy: Leap seconds will get lost as well as micro- or nanoseconds.
	 * 
	 * @param certVersion version of certificate.
	 * @param timeStamp java date timestamp to convert
	 */
	public Time64(int certVersion, Date timeStamp) {		
		Moment moment = TemporalType.JAVA_UTIL_DATE.translate(timeStamp);
		BigDecimal bd = moment.transform(TimeScale.TAI);
		if(certVersion == Certificate.CERTIFICATE_VERSION_1){
		  this.timeStamp = bd.subtract(new BigDecimal(SECONDSBETWEENTAIZEROAND2010)).multiply(new BigDecimal(1000000)).toBigInteger();
		}else{
		  this.timeStamp = bd.subtract(new BigDecimal(SECONDSBETWEENTAIZEROAND2004)).multiply(new BigDecimal(1000000)).toBigInteger();
		}
	}
	
	/**
	 * Main constructor for Time64
	 * 
	 * @param elapsedTime  the number of microseconds since 1 Jan 2010 TAI
	 */
	public Time64(BigInteger elapsedTime) {		
		this.timeStamp =elapsedTime;
	}
	
	/**
	 * Constructor used during serializing.
	 * 
	 */
	public Time64(){
	
		
	}
	
	/** 
	 * Returns the timestamp as a Java util date.
	 * 
	 * @param certVersion indicating with certificate version to parse the value as, since the different
	 * versions have different base times.
	 * 
	 * <b>Important: Note that this transformation is sometimes lossy: Leap seconds will get lost as well as micro- or nanoseconds.
	 * @return the timestamp value
	 */
	public Date asDate(int certVersion){
		long elapsedTime = this.timeStamp.divide(new BigInteger("1000000")).longValue();
		int nanoSeconds = this.timeStamp.remainder(new BigInteger("1000000")).intValue();
		Moment m;
		if(certVersion == Certificate.CERTIFICATE_VERSION_1){
			m = Moment.of(elapsedTime + SECONDSBETWEENTAIZEROAND2010,nanoSeconds, TimeScale.TAI);
		}else{
			m = Moment.of(elapsedTime + SECONDSBETWEENTAIZEROAND2004,nanoSeconds, TimeScale.TAI);
		}
		return TemporalType.JAVA_UTIL_DATE.from(m);
	}
	
	/**
	 * 
	 * @return the number of microseconds since 1 Jan 2010 TAI
	 */
	public BigInteger asElapsedTime(){
		return timeStamp;
	}


	@Override
	public void encode(DataOutputStream out) throws IOException {
		byte[] data = timeStamp.toByteArray();
		if(data.length > 8){
			out.write(data, data.length-8, 8);
		}else{
			byte[] padding = new byte[8-data.length];
			out.write(padding);
			out.write(data);
		}		
	}

	@Override
	public void decode(DataInputStream in) throws IOException {
		byte[] data = new byte[9];
		in.read(data,1,8);
		timeStamp = new BigInteger(data);
	}

	@Override
	public String toString() {
		return "Time64 ["+ timeStamp + "]";
	}
	
	/**
	 * Alternative toString version to get more verbose information
	 * @param certVersion the version of the certificate.
	 * @return a more verbose string version.
	 */
	public String toString(int certVersion) {
		return "Time64 [" + asDate(certVersion) + " (" + timeStamp + ")]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((timeStamp == null) ? 0 : timeStamp.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Time64 other = (Time64) obj;
		if (timeStamp == null) {
			if (other.timeStamp != null)
				return false;
		} else if (!timeStamp.equals(other.timeStamp))
			return false;
		return true;
	}



	
}
