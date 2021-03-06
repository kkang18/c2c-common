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
package org.certificateservices.custom.c2x.ieee1609dot2.datastructs.basic

import org.certificateservices.custom.c2x.common.BaseStructSpec;
import org.certificateservices.custom.c2x.common.crypto.Algorithm
import org.certificateservices.custom.c2x.common.crypto.DefaultCryptoManagerParams;
import org.certificateservices.custom.c2x.ieee1609dot2.datastructs.basic.Duration.DurationChoices;
import org.certificateservices.custom.c2x.ieee1609dot2.datastructs.basic.SymmAlgorithm;

import spock.lang.Specification;
import spock.lang.Unroll;

/**
 * Test for SymmAlgorithm
 * 
 * @author Philip Vendil, p.vendil@cgi.com
 *
 */
class SymmAlgorithmSpec extends BaseStructSpec {


	def "Verify correct algorithms indicator is returned."(){
		when:
		Algorithm alg = SymmAlgorithm.aes128Ccm.getAlgorithm()
		then:
		alg.getHash() == Algorithm.Hash.sha256
		alg.getSymmetric() == Algorithm.Symmetric.aes128Ccm
		alg.getSignature() == null
		alg.getEncryption() == null
		
	}
	
}
