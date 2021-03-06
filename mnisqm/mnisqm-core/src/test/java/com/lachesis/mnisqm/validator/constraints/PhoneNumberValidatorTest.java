/**
 * This file is part of mnisqm-core.
 *
 * mnisqm-core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mnisqm-core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mnisqm-core.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.lachesis.mnisqm.validator.constraints;

import org.junit.Assert;
import org.junit.Test;

import com.lachesis.mnisqm.core.utils.StringUtils;

public class PhoneNumberValidatorTest {

	@Test
	public void testPhoneNumber() {
		Assert.assertEquals(true, StringUtils.isValidPhoneNumber("0918734068"));
		Assert.assertEquals(false, StringUtils.isValidPhoneNumber("a"));
		Assert.assertEquals(false, StringUtils.isValidPhoneNumber("091a"));
		Assert.assertEquals(true, StringUtils.isValidPhoneNumber("1111111111"));
		Assert.assertEquals(false, StringUtils.isValidPhoneNumber("1111"));
		Assert.assertEquals(true, StringUtils.isValidPhoneNumber("(111)-111-1111"));
	}
}
