package org.jdom2.test.cases;

import static org.junit.Assert.*;

import org.jdom2.Verifier;
import org.junit.Test;

public class TestVerifierCharacters {


	// Automated test built by VerifierTestBuilder
	@Test
	public void testIsHighSurrogate() {
		int c = 0;
		long ms = System.currentTimeMillis();
		while (c < 0xd800) {
			if (Verifier.isHighSurrogate((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isHighSurrogate but it did not.");
			}
			c++;
		}

		while (c < 0xdc00) {
			if (!Verifier.isHighSurrogate((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isHighSurrogate but it did not.");
			}
			c++;
		}

		while (c < 0x10000) {
			if (Verifier.isHighSurrogate((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isHighSurrogate but it did not.");
			}
			c++;
		}

		System.out.printf("Completed test testIsHighSurrogate in %dms\n", System.currentTimeMillis() - ms);
	}

	// Automated test built by VerifierTestBuilder
	@Test
	public void testIsLowSurrogate() {
		int c = 0;
		long ms = System.currentTimeMillis();
		while (c < 0xdc00) {
			if (Verifier.isLowSurrogate((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isLowSurrogate but it did not.");
			}
			c++;
		}

		while (c < 0xe000) {
			if (!Verifier.isLowSurrogate((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isLowSurrogate but it did not.");
			}
			c++;
		}

		while (c < 0x10000) {
			if (Verifier.isLowSurrogate((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isLowSurrogate but it did not.");
			}
			c++;
		}

		System.out.printf("Completed test testIsLowSurrogate in %dms\n", System.currentTimeMillis() - ms);
	}

	// Automated test built by VerifierTestBuilder
	@Test
	public void testIsXMLDigit() {
		int c = 0;
		long ms = System.currentTimeMillis();
		while (c < 0x0030) {
			if (Verifier.isXMLDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLDigit but it did not.");
			}
			c++;
		}

		while (c < 0x003a) {
			if (!Verifier.isXMLDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0660) {
			if (Verifier.isXMLDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLDigit but it did not.");
			}
			c++;
		}

		while (c < 0x066a) {
			if (!Verifier.isXMLDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLDigit but it did not.");
			}
			c++;
		}

		while (c < 0x06f0) {
			if (Verifier.isXMLDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLDigit but it did not.");
			}
			c++;
		}

		while (c < 0x06fa) {
			if (!Verifier.isXMLDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0966) {
			if (Verifier.isXMLDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0970) {
			if (!Verifier.isXMLDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLDigit but it did not.");
			}
			c++;
		}

		while (c < 0x09e6) {
			if (Verifier.isXMLDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLDigit but it did not.");
			}
			c++;
		}

		while (c < 0x09f0) {
			if (!Verifier.isXMLDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0a66) {
			if (Verifier.isXMLDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0a70) {
			if (!Verifier.isXMLDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0ae6) {
			if (Verifier.isXMLDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0af0) {
			if (!Verifier.isXMLDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0b66) {
			if (Verifier.isXMLDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0b70) {
			if (!Verifier.isXMLDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0be7) {
			if (Verifier.isXMLDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0bf0) {
			if (!Verifier.isXMLDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0c66) {
			if (Verifier.isXMLDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0c70) {
			if (!Verifier.isXMLDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0ce6) {
			if (Verifier.isXMLDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0cf0) {
			if (!Verifier.isXMLDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0d66) {
			if (Verifier.isXMLDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0d70) {
			if (!Verifier.isXMLDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0e50) {
			if (Verifier.isXMLDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0e5a) {
			if (!Verifier.isXMLDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0ed0) {
			if (Verifier.isXMLDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0eda) {
			if (!Verifier.isXMLDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0f20) {
			if (Verifier.isXMLDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0f2a) {
			if (!Verifier.isXMLDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLDigit but it did not.");
			}
			c++;
		}

		while (c < 0x10000) {
			if (Verifier.isXMLDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLDigit but it did not.");
			}
			c++;
		}

		System.out.printf("Completed test testIsXMLDigit in %dms\n", System.currentTimeMillis() - ms);
	}

	// Automated test built by VerifierTestBuilder
	@Test
	public void testIsXMLNameCharacter() {
		int c = 0;
		long ms = System.currentTimeMillis();
		while (c < 0x002d) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x002f) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0030) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x003b) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0041) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x005b) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x005f) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0060) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0061) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x007b) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x00b7) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x00b8) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x00c0) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x00d7) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x00d8) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x00f7) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x00f8) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0132) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0134) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x013f) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0141) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0149) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x014a) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x017f) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0180) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x01c4) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x01cd) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x01f1) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x01f4) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x01f6) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x01fa) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0218) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0250) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x02a9) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x02bb) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x02c2) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x02d0) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x02d2) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0300) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0346) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0360) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0362) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0386) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x038b) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x038c) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x038d) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x038e) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x03a2) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x03a3) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x03cf) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x03d0) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x03d7) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x03da) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x03db) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x03dc) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x03dd) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x03de) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x03df) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x03e0) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x03e1) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x03e2) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x03f4) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0401) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x040d) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x040e) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0450) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0451) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x045d) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x045e) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0482) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0483) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0487) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0490) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x04c5) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x04c7) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x04c9) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x04cb) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x04cd) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x04d0) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x04ec) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x04ee) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x04f6) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x04f8) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x04fa) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0531) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0557) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0559) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x055a) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0561) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0587) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0591) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x05a2) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x05a3) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x05ba) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x05bb) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x05be) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x05bf) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x05c0) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x05c1) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x05c3) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x05c4) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x05c5) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x05d0) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x05eb) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x05f0) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x05f3) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0621) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x063b) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0640) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0653) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0660) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x066a) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0670) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x06b8) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x06ba) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x06bf) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x06c0) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x06cf) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x06d0) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x06d4) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x06d5) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x06e9) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x06ea) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x06ee) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x06f0) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x06fa) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0901) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0904) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0905) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x093a) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x093c) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x094e) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0951) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0955) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0958) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0964) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0966) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0970) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0981) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0984) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0985) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x098d) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x098f) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0991) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0993) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x09a9) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x09aa) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x09b1) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x09b2) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x09b3) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x09b6) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x09ba) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x09bc) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x09bd) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x09be) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x09c5) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x09c7) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x09c9) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x09cb) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x09ce) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x09d7) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x09d8) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x09dc) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x09de) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x09df) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x09e4) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x09e6) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x09f2) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a02) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a03) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a05) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a0b) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a0f) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a11) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a13) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a29) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a2a) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a31) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a32) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a34) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a35) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a37) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a38) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a3a) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a3c) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a3d) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a3e) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a43) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a47) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a49) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a4b) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a4e) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a59) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a5d) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a5e) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a5f) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a66) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a75) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a81) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a84) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a85) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a8c) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a8d) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a8e) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a8f) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a92) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a93) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0aa9) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0aaa) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ab1) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ab2) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ab4) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ab5) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0aba) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0abc) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ac6) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ac7) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0aca) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0acb) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ace) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ae0) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ae1) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ae6) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0af0) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b01) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b04) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b05) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b0d) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b0f) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b11) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b13) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b29) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b2a) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b31) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b32) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b34) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b36) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b3a) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b3c) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b44) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b47) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b49) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b4b) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b4e) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b56) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b58) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b5c) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b5e) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b5f) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b62) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b66) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b70) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b82) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b84) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b85) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b8b) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b8e) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b91) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b92) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b96) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b99) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b9b) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b9c) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b9d) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b9e) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ba0) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ba3) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ba5) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ba8) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0bab) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0bae) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0bb6) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0bb7) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0bba) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0bbe) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0bc3) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0bc6) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0bc9) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0bca) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0bce) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0bd7) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0bd8) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0be7) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0bf0) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c01) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c04) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c05) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c0d) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c0e) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c11) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c12) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c29) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c2a) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c34) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c35) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c3a) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c3e) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c45) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c46) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c49) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c4a) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c4e) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c55) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c57) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c60) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c62) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c66) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c70) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c82) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c84) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c85) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c8d) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c8e) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c91) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c92) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ca9) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0caa) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0cb4) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0cb5) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0cba) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0cbe) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0cc5) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0cc6) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0cc9) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0cca) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0cce) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0cd5) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0cd7) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0cde) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0cdf) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ce0) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ce2) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ce6) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0cf0) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0d02) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0d04) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0d05) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0d0d) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0d0e) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0d11) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0d12) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0d29) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0d2a) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0d3a) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0d3e) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0d44) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0d46) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0d49) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0d4a) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0d4e) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0d57) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0d58) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0d60) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0d62) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0d66) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0d70) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0e01) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0e2f) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0e30) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0e3b) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0e40) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0e4f) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0e50) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0e5a) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0e81) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0e83) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0e84) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0e85) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0e87) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0e89) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0e8a) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0e8b) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0e8d) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0e8e) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0e94) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0e98) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0e99) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ea0) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ea1) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ea4) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ea5) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ea6) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ea7) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ea8) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0eaa) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0eac) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ead) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0eaf) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0eb0) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0eba) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ebb) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ebe) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ec0) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ec5) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ec6) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ec7) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ec8) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ece) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ed0) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0eda) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0f18) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0f1a) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0f20) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0f2a) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0f35) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0f36) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0f37) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0f38) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0f39) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0f3a) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0f3e) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0f48) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0f49) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0f6a) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0f71) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0f85) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0f86) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0f8c) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0f90) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0f96) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0f97) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0f98) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0f99) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0fae) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0fb1) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0fb8) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0fb9) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0fba) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x10a0) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x10c6) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x10d0) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x10f7) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1100) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1101) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1102) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1104) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1105) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1108) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1109) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x110a) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x110b) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x110d) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x110e) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1113) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x113c) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x113d) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x113e) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x113f) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1140) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1141) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x114c) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x114d) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x114e) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x114f) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1150) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1151) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1154) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1156) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1159) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x115a) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x115f) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1162) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1163) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1164) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1165) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1166) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1167) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1168) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1169) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x116a) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x116d) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x116f) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1172) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1174) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1175) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1176) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x119e) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x119f) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x11a8) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x11a9) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x11ab) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x11ac) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x11ae) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x11b0) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x11b7) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x11b9) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x11ba) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x11bb) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x11bc) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x11c3) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x11eb) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x11ec) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x11f0) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x11f1) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x11f9) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x11fa) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1e00) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1e9c) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1ea0) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1efa) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1f00) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1f16) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1f18) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1f1e) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1f20) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1f46) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1f48) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1f4e) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1f50) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1f58) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1f59) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1f5a) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1f5b) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1f5c) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1f5d) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1f5e) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1f5f) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1f7e) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1f80) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1fb5) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1fb6) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1fbd) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1fbe) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1fbf) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1fc2) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1fc5) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1fc6) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1fcd) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1fd0) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1fd4) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1fd6) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1fdc) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1fe0) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1fed) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1ff2) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1ff5) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1ff6) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1ffd) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x20d0) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x20dd) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x20e1) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x20e2) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x2126) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x2127) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x212a) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x212c) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x212e) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x212f) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x2180) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x2183) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x3005) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x3006) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x3007) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x3008) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x3021) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x3030) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x3031) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x3036) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x3041) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x3095) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x3099) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x309b) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x309d) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x309f) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x30a1) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x30fb) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x30fc) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x30ff) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x3105) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x312d) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x4e00) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x9fa6) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0xac00) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0xd7a4) {
			if (!Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x10000) {
			if (Verifier.isXMLNameCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameCharacter but it did not.");
			}
			c++;
		}

		System.out.printf("Completed test testIsXMLNameCharacter in %dms\n", System.currentTimeMillis() - ms);
	}

	// Automated test built by VerifierTestBuilder
	@Test
	public void testIsXMLPublicIDCharacter() {
		int c = 0;
		long ms = System.currentTimeMillis();
		while (c < 0x0009) {
			if (Verifier.isXMLPublicIDCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLPublicIDCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x000b) {
			if (!Verifier.isXMLPublicIDCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLPublicIDCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x000d) {
			if (Verifier.isXMLPublicIDCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLPublicIDCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x000e) {
			if (!Verifier.isXMLPublicIDCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLPublicIDCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0020) {
			if (Verifier.isXMLPublicIDCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLPublicIDCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0022) {
			if (!Verifier.isXMLPublicIDCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLPublicIDCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0023) {
			if (Verifier.isXMLPublicIDCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLPublicIDCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0026) {
			if (!Verifier.isXMLPublicIDCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLPublicIDCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0027) {
			if (Verifier.isXMLPublicIDCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLPublicIDCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x003c) {
			if (!Verifier.isXMLPublicIDCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLPublicIDCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x003d) {
			if (Verifier.isXMLPublicIDCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLPublicIDCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x003e) {
			if (!Verifier.isXMLPublicIDCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLPublicIDCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x003f) {
			if (Verifier.isXMLPublicIDCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLPublicIDCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x005b) {
			if (!Verifier.isXMLPublicIDCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLPublicIDCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x005f) {
			if (Verifier.isXMLPublicIDCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLPublicIDCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0060) {
			if (!Verifier.isXMLPublicIDCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLPublicIDCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0061) {
			if (Verifier.isXMLPublicIDCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLPublicIDCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x007b) {
			if (!Verifier.isXMLPublicIDCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLPublicIDCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x10000) {
			if (Verifier.isXMLPublicIDCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLPublicIDCharacter but it did not.");
			}
			c++;
		}

		System.out.printf("Completed test testIsXMLPublicIDCharacter in %dms\n", System.currentTimeMillis() - ms);
	}

	// Automated test built by VerifierTestBuilder
	@Test
	public void testIsXMLNameStartCharacter() {
		int c = 0;
		long ms = System.currentTimeMillis();
		while (c < 0x003a) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x003b) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0041) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x005b) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x005f) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0060) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0061) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x007b) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x00c0) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x00d7) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x00d8) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x00f7) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x00f8) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0132) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0134) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x013f) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0141) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0149) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x014a) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x017f) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0180) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x01c4) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x01cd) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x01f1) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x01f4) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x01f6) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x01fa) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0218) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0250) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x02a9) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x02bb) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x02c2) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0386) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0387) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0388) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x038b) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x038c) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x038d) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x038e) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x03a2) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x03a3) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x03cf) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x03d0) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x03d7) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x03da) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x03db) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x03dc) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x03dd) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x03de) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x03df) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x03e0) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x03e1) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x03e2) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x03f4) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0401) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x040d) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x040e) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0450) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0451) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x045d) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x045e) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0482) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0490) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x04c5) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x04c7) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x04c9) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x04cb) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x04cd) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x04d0) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x04ec) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x04ee) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x04f6) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x04f8) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x04fa) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0531) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0557) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0559) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x055a) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0561) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0587) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x05d0) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x05eb) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x05f0) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x05f3) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0621) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x063b) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0641) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x064b) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0671) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x06b8) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x06ba) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x06bf) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x06c0) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x06cf) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x06d0) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x06d4) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x06d5) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x06d6) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x06e5) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x06e7) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0905) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x093a) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x093d) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x093e) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0958) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0962) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0985) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x098d) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x098f) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0991) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0993) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x09a9) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x09aa) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x09b1) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x09b2) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x09b3) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x09b6) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x09ba) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x09dc) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x09de) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x09df) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x09e2) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x09f0) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x09f2) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a05) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a0b) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a0f) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a11) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a13) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a29) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a2a) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a31) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a32) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a34) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a35) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a37) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a38) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a3a) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a59) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a5d) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a5e) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a5f) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a72) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a75) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a85) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a8c) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a8d) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a8e) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a8f) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a92) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0a93) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0aa9) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0aaa) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ab1) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ab2) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ab4) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ab5) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0aba) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0abd) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0abe) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ae0) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ae1) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b05) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b0d) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b0f) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b11) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b13) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b29) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b2a) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b31) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b32) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b34) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b36) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b3a) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b3d) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b3e) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b5c) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b5e) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b5f) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b62) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b85) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b8b) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b8e) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b91) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b92) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b96) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b99) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b9b) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b9c) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b9d) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0b9e) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ba0) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ba3) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ba5) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ba8) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0bab) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0bae) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0bb6) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0bb7) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0bba) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c05) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c0d) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c0e) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c11) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c12) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c29) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c2a) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c34) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c35) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c3a) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c60) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c62) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c85) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c8d) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c8e) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c91) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0c92) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ca9) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0caa) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0cb4) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0cb5) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0cba) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0cde) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0cdf) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ce0) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ce2) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0d05) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0d0d) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0d0e) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0d11) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0d12) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0d29) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0d2a) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0d3a) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0d60) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0d62) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0e01) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0e2f) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0e30) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0e31) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0e32) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0e34) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0e40) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0e46) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0e81) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0e83) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0e84) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0e85) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0e87) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0e89) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0e8a) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0e8b) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0e8d) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0e8e) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0e94) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0e98) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0e99) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ea0) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ea1) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ea4) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ea5) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ea6) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ea7) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ea8) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0eaa) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0eac) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ead) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0eaf) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0eb0) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0eb1) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0eb2) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0eb4) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ebd) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ebe) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ec0) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0ec5) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0f40) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0f48) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0f49) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0f6a) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x10a0) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x10c6) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x10d0) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x10f7) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1100) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1101) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1102) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1104) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1105) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1108) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1109) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x110a) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x110b) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x110d) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x110e) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1113) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x113c) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x113d) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x113e) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x113f) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1140) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1141) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x114c) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x114d) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x114e) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x114f) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1150) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1151) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1154) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1156) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1159) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x115a) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x115f) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1162) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1163) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1164) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1165) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1166) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1167) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1168) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1169) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x116a) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x116d) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x116f) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1172) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1174) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1175) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1176) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x119e) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x119f) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x11a8) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x11a9) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x11ab) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x11ac) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x11ae) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x11b0) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x11b7) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x11b9) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x11ba) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x11bb) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x11bc) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x11c3) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x11eb) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x11ec) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x11f0) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x11f1) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x11f9) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x11fa) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1e00) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1e9c) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1ea0) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1efa) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1f00) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1f16) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1f18) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1f1e) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1f20) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1f46) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1f48) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1f4e) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1f50) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1f58) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1f59) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1f5a) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1f5b) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1f5c) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1f5d) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1f5e) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1f5f) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1f7e) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1f80) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1fb5) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1fb6) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1fbd) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1fbe) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1fbf) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1fc2) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1fc5) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1fc6) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1fcd) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1fd0) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1fd4) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1fd6) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1fdc) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1fe0) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1fed) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1ff2) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1ff5) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1ff6) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x1ffd) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x2126) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x2127) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x212a) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x212c) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x212e) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x212f) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x2180) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x2183) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x3007) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x3008) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x3021) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x302a) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x3041) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x3095) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x30a1) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x30fb) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x3105) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x312d) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x4e00) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x9fa6) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0xac00) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0xd7a4) {
			if (!Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		while (c < 0x10000) {
			if (Verifier.isXMLNameStartCharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLNameStartCharacter but it did not.");
			}
			c++;
		}

		System.out.printf("Completed test testIsXMLNameStartCharacter in %dms\n", System.currentTimeMillis() - ms);
	}

	// Automated test built by VerifierTestBuilder
	@Test
	public void testIsURICharacter() {
		int c = 0;
		long ms = System.currentTimeMillis();
		while (c < 0x0021) {
			if (Verifier.isURICharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isURICharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0022) {
			if (!Verifier.isURICharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isURICharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0024) {
			if (Verifier.isURICharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isURICharacter but it did not.");
			}
			c++;
		}

		while (c < 0x003b) {
			if (!Verifier.isURICharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isURICharacter but it did not.");
			}
			c++;
		}

		while (c < 0x003d) {
			if (Verifier.isURICharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isURICharacter but it did not.");
			}
			c++;
		}

		while (c < 0x003e) {
			if (!Verifier.isURICharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isURICharacter but it did not.");
			}
			c++;
		}

		while (c < 0x003f) {
			if (Verifier.isURICharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isURICharacter but it did not.");
			}
			c++;
		}

		while (c < 0x005b) {
			if (!Verifier.isURICharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isURICharacter but it did not.");
			}
			c++;
		}

		while (c < 0x005f) {
			if (Verifier.isURICharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isURICharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0060) {
			if (!Verifier.isURICharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isURICharacter but it did not.");
			}
			c++;
		}

		while (c < 0x0061) {
			if (Verifier.isURICharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isURICharacter but it did not.");
			}
			c++;
		}

		while (c < 0x007b) {
			if (!Verifier.isURICharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isURICharacter but it did not.");
			}
			c++;
		}

		while (c < 0x007e) {
			if (Verifier.isURICharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isURICharacter but it did not.");
			}
			c++;
		}

		while (c < 0x007f) {
			if (!Verifier.isURICharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isURICharacter but it did not.");
			}
			c++;
		}

		while (c < 0x10000) {
			if (Verifier.isURICharacter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isURICharacter but it did not.");
			}
			c++;
		}

		System.out.printf("Completed test testIsURICharacter in %dms\n", System.currentTimeMillis() - ms);
	}

	// Automated test built by VerifierTestBuilder
	@Test
	public void testIsHexDigit() {
		int c = 0;
		long ms = System.currentTimeMillis();
		while (c < 0x0030) {
			if (Verifier.isHexDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isHexDigit but it did not.");
			}
			c++;
		}

		while (c < 0x003a) {
			if (!Verifier.isHexDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isHexDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0041) {
			if (Verifier.isHexDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isHexDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0047) {
			if (!Verifier.isHexDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isHexDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0061) {
			if (Verifier.isHexDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isHexDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0067) {
			if (!Verifier.isHexDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isHexDigit but it did not.");
			}
			c++;
		}

		while (c < 0x10000) {
			if (Verifier.isHexDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isHexDigit but it did not.");
			}
			c++;
		}

		System.out.printf("Completed test testIsHexDigit in %dms\n", System.currentTimeMillis() - ms);
	}

	// Automated test built by VerifierTestBuilder
	@Test
	public void testIsXMLLetter() {
		int c = 0;
		long ms = System.currentTimeMillis();
		while (c < 0x0041) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x005b) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0061) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x007b) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x00c0) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x00d7) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x00d8) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x00f7) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x00f8) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0132) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0134) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x013f) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0141) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0149) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x014a) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x017f) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0180) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x01c4) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x01cd) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x01f1) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x01f4) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x01f6) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x01fa) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0218) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0250) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x02a9) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x02bb) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x02c2) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0386) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0387) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0388) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x038b) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x038c) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x038d) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x038e) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x03a2) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x03a3) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x03cf) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x03d0) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x03d7) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x03da) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x03db) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x03dc) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x03dd) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x03de) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x03df) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x03e0) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x03e1) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x03e2) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x03f4) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0401) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x040d) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x040e) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0450) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0451) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x045d) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x045e) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0482) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0490) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x04c5) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x04c7) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x04c9) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x04cb) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x04cd) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x04d0) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x04ec) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x04ee) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x04f6) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x04f8) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x04fa) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0531) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0557) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0559) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x055a) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0561) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0587) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x05d0) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x05eb) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x05f0) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x05f3) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0621) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x063b) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0641) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x064b) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0671) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x06b8) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x06ba) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x06bf) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x06c0) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x06cf) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x06d0) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x06d4) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x06d5) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x06d6) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x06e5) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x06e7) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0905) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x093a) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x093d) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x093e) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0958) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0962) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0985) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x098d) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x098f) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0991) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0993) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x09a9) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x09aa) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x09b1) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x09b2) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x09b3) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x09b6) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x09ba) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x09dc) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x09de) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x09df) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x09e2) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x09f0) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x09f2) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0a05) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0a0b) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0a0f) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0a11) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0a13) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0a29) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0a2a) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0a31) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0a32) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0a34) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0a35) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0a37) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0a38) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0a3a) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0a59) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0a5d) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0a5e) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0a5f) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0a72) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0a75) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0a85) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0a8c) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0a8d) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0a8e) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0a8f) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0a92) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0a93) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0aa9) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0aaa) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0ab1) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0ab2) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0ab4) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0ab5) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0aba) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0abd) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0abe) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0ae0) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0ae1) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0b05) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0b0d) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0b0f) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0b11) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0b13) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0b29) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0b2a) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0b31) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0b32) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0b34) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0b36) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0b3a) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0b3d) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0b3e) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0b5c) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0b5e) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0b5f) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0b62) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0b85) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0b8b) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0b8e) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0b91) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0b92) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0b96) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0b99) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0b9b) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0b9c) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0b9d) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0b9e) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0ba0) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0ba3) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0ba5) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0ba8) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0bab) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0bae) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0bb6) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0bb7) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0bba) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0c05) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0c0d) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0c0e) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0c11) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0c12) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0c29) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0c2a) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0c34) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0c35) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0c3a) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0c60) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0c62) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0c85) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0c8d) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0c8e) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0c91) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0c92) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0ca9) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0caa) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0cb4) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0cb5) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0cba) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0cde) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0cdf) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0ce0) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0ce2) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0d05) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0d0d) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0d0e) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0d11) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0d12) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0d29) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0d2a) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0d3a) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0d60) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0d62) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0e01) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0e2f) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0e30) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0e31) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0e32) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0e34) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0e40) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0e46) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0e81) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0e83) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0e84) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0e85) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0e87) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0e89) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0e8a) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0e8b) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0e8d) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0e8e) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0e94) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0e98) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0e99) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0ea0) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0ea1) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0ea4) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0ea5) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0ea6) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0ea7) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0ea8) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0eaa) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0eac) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0ead) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0eaf) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0eb0) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0eb1) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0eb2) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0eb4) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0ebd) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0ebe) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0ec0) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0ec5) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0f40) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0f48) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0f49) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x0f6a) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x10a0) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x10c6) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x10d0) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x10f7) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1100) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1101) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1102) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1104) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1105) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1108) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1109) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x110a) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x110b) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x110d) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x110e) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1113) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x113c) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x113d) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x113e) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x113f) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1140) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1141) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x114c) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x114d) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x114e) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x114f) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1150) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1151) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1154) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1156) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1159) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x115a) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x115f) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1162) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1163) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1164) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1165) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1166) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1167) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1168) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1169) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x116a) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x116d) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x116f) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1172) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1174) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1175) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1176) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x119e) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x119f) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x11a8) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x11a9) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x11ab) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x11ac) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x11ae) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x11b0) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x11b7) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x11b9) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x11ba) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x11bb) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x11bc) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x11c3) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x11eb) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x11ec) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x11f0) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x11f1) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x11f9) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x11fa) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1e00) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1e9c) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1ea0) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1efa) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1f00) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1f16) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1f18) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1f1e) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1f20) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1f46) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1f48) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1f4e) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1f50) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1f58) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1f59) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1f5a) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1f5b) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1f5c) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1f5d) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1f5e) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1f5f) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1f7e) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1f80) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1fb5) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1fb6) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1fbd) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1fbe) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1fbf) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1fc2) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1fc5) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1fc6) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1fcd) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1fd0) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1fd4) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1fd6) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1fdc) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1fe0) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1fed) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1ff2) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1ff5) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1ff6) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x1ffd) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x2126) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x2127) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x212a) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x212c) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x212e) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x212f) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x2180) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x2183) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x3007) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x3008) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x3021) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x302a) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x3041) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x3095) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x30a1) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x30fb) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x3105) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x312d) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x4e00) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x9fa6) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0xac00) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0xd7a4) {
			if (!Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetter but it did not.");
			}
			c++;
		}

		while (c < 0x10000) {
			if (Verifier.isXMLLetter((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetter but it did not.");
			}
			c++;
		}

		System.out.printf("Completed test testIsXMLLetter in %dms\n", System.currentTimeMillis() - ms);
	}

	// Automated test built by VerifierTestBuilder
	@Test
	public void testIsXMLCombiningChar() {
		int c = 0;
		long ms = System.currentTimeMillis();
		while (c < 0x0300) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0346) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0360) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0362) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0483) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0487) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0591) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x05a2) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x05a3) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x05ba) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x05bb) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x05be) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x05bf) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x05c0) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x05c1) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x05c3) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x05c4) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x05c5) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x064b) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0653) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0670) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0671) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x06d6) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x06e5) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x06e7) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x06e9) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x06ea) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x06ee) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0901) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0904) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x093c) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x093d) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x093e) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x094e) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0951) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0955) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0962) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0964) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0981) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0984) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x09bc) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x09bd) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x09be) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x09c5) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x09c7) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x09c9) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x09cb) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x09ce) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x09d7) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x09d8) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x09e2) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x09e4) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0a02) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0a03) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0a3c) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0a3d) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0a3e) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0a43) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0a47) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0a49) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0a4b) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0a4e) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0a70) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0a72) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0a81) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0a84) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0abc) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0abd) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0abe) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0ac6) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0ac7) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0aca) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0acb) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0ace) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0b01) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0b04) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0b3c) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0b3d) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0b3e) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0b44) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0b47) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0b49) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0b4b) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0b4e) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0b56) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0b58) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0b82) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0b84) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0bbe) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0bc3) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0bc6) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0bc9) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0bca) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0bce) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0bd7) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0bd8) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0c01) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0c04) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0c3e) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0c45) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0c46) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0c49) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0c4a) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0c4e) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0c55) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0c57) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0c82) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0c84) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0cbe) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0cc5) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0cc6) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0cc9) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0cca) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0cce) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0cd5) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0cd7) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0d02) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0d04) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0d3e) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0d44) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0d46) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0d49) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0d4a) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0d4e) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0d57) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0d58) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0e31) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0e32) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0e34) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0e3b) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0e47) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0e4f) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0eb1) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0eb2) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0eb4) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0eba) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0ebb) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0ebd) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0ec8) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0ece) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0f18) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0f1a) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0f35) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0f36) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0f37) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0f38) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0f39) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0f3a) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0f3e) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0f40) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0f71) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0f85) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0f86) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0f8c) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0f90) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0f96) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0f97) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0f98) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0f99) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0fae) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0fb1) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0fb8) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0fb9) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x0fba) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x20d0) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x20dd) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x20e1) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x20e2) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x302a) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x3030) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x3099) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x309b) {
			if (!Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLCombiningChar but it did not.");
			}
			c++;
		}

		while (c < 0x10000) {
			if (Verifier.isXMLCombiningChar((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLCombiningChar but it did not.");
			}
			c++;
		}

		System.out.printf("Completed test testIsXMLCombiningChar in %dms\n", System.currentTimeMillis() - ms);
	}

	// Automated test built by VerifierTestBuilder
	@Test
	public void testIsXMLExtender() {
		int c = 0;
		long ms = System.currentTimeMillis();
		while (c < 0x00b7) {
			if (Verifier.isXMLExtender((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLExtender but it did not.");
			}
			c++;
		}

		while (c < 0x00b8) {
			if (!Verifier.isXMLExtender((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLExtender but it did not.");
			}
			c++;
		}

		while (c < 0x02d0) {
			if (Verifier.isXMLExtender((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLExtender but it did not.");
			}
			c++;
		}

		while (c < 0x02d2) {
			if (!Verifier.isXMLExtender((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLExtender but it did not.");
			}
			c++;
		}

		while (c < 0x0387) {
			if (Verifier.isXMLExtender((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLExtender but it did not.");
			}
			c++;
		}

		while (c < 0x0388) {
			if (!Verifier.isXMLExtender((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLExtender but it did not.");
			}
			c++;
		}

		while (c < 0x0640) {
			if (Verifier.isXMLExtender((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLExtender but it did not.");
			}
			c++;
		}

		while (c < 0x0641) {
			if (!Verifier.isXMLExtender((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLExtender but it did not.");
			}
			c++;
		}

		while (c < 0x0e46) {
			if (Verifier.isXMLExtender((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLExtender but it did not.");
			}
			c++;
		}

		while (c < 0x0e47) {
			if (!Verifier.isXMLExtender((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLExtender but it did not.");
			}
			c++;
		}

		while (c < 0x0ec6) {
			if (Verifier.isXMLExtender((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLExtender but it did not.");
			}
			c++;
		}

		while (c < 0x0ec7) {
			if (!Verifier.isXMLExtender((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLExtender but it did not.");
			}
			c++;
		}

		while (c < 0x3005) {
			if (Verifier.isXMLExtender((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLExtender but it did not.");
			}
			c++;
		}

		while (c < 0x3006) {
			if (!Verifier.isXMLExtender((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLExtender but it did not.");
			}
			c++;
		}

		while (c < 0x3031) {
			if (Verifier.isXMLExtender((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLExtender but it did not.");
			}
			c++;
		}

		while (c < 0x3036) {
			if (!Verifier.isXMLExtender((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLExtender but it did not.");
			}
			c++;
		}

		while (c < 0x309d) {
			if (Verifier.isXMLExtender((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLExtender but it did not.");
			}
			c++;
		}

		while (c < 0x309f) {
			if (!Verifier.isXMLExtender((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLExtender but it did not.");
			}
			c++;
		}

		while (c < 0x30fc) {
			if (Verifier.isXMLExtender((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLExtender but it did not.");
			}
			c++;
		}

		while (c < 0x30ff) {
			if (!Verifier.isXMLExtender((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLExtender but it did not.");
			}
			c++;
		}

		while (c < 0x10000) {
			if (Verifier.isXMLExtender((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLExtender but it did not.");
			}
			c++;
		}

		System.out.printf("Completed test testIsXMLExtender in %dms\n", System.currentTimeMillis() - ms);
	}

	// Automated test built by VerifierTestBuilder
	@Test
	public void testIsXMLLetterOrDigit() {
		int c = 0;
		long ms = System.currentTimeMillis();
		while (c < 0x0030) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x003a) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0041) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x005b) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0061) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x007b) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x00c0) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x00d7) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x00d8) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x00f7) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x00f8) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0132) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0134) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x013f) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0141) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0149) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x014a) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x017f) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0180) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x01c4) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x01cd) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x01f1) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x01f4) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x01f6) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x01fa) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0218) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0250) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x02a9) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x02bb) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x02c2) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0386) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0387) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0388) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x038b) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x038c) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x038d) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x038e) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x03a2) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x03a3) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x03cf) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x03d0) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x03d7) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x03da) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x03db) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x03dc) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x03dd) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x03de) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x03df) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x03e0) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x03e1) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x03e2) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x03f4) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0401) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x040d) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x040e) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0450) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0451) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x045d) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x045e) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0482) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0490) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x04c5) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x04c7) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x04c9) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x04cb) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x04cd) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x04d0) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x04ec) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x04ee) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x04f6) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x04f8) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x04fa) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0531) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0557) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0559) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x055a) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0561) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0587) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x05d0) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x05eb) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x05f0) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x05f3) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0621) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x063b) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0641) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x064b) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0660) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x066a) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0671) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x06b8) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x06ba) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x06bf) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x06c0) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x06cf) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x06d0) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x06d4) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x06d5) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x06d6) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x06e5) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x06e7) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x06f0) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x06fa) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0905) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x093a) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x093d) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x093e) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0958) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0962) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0966) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0970) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0985) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x098d) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x098f) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0991) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0993) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x09a9) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x09aa) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x09b1) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x09b2) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x09b3) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x09b6) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x09ba) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x09dc) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x09de) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x09df) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x09e2) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x09e6) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x09f2) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0a05) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0a0b) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0a0f) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0a11) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0a13) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0a29) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0a2a) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0a31) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0a32) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0a34) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0a35) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0a37) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0a38) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0a3a) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0a59) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0a5d) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0a5e) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0a5f) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0a66) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0a70) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0a72) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0a75) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0a85) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0a8c) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0a8d) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0a8e) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0a8f) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0a92) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0a93) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0aa9) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0aaa) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0ab1) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0ab2) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0ab4) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0ab5) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0aba) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0abd) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0abe) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0ae0) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0ae1) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0ae6) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0af0) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0b05) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0b0d) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0b0f) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0b11) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0b13) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0b29) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0b2a) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0b31) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0b32) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0b34) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0b36) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0b3a) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0b3d) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0b3e) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0b5c) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0b5e) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0b5f) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0b62) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0b66) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0b70) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0b85) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0b8b) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0b8e) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0b91) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0b92) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0b96) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0b99) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0b9b) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0b9c) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0b9d) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0b9e) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0ba0) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0ba3) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0ba5) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0ba8) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0bab) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0bae) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0bb6) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0bb7) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0bba) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0be7) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0bf0) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0c05) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0c0d) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0c0e) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0c11) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0c12) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0c29) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0c2a) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0c34) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0c35) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0c3a) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0c60) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0c62) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0c66) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0c70) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0c85) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0c8d) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0c8e) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0c91) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0c92) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0ca9) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0caa) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0cb4) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0cb5) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0cba) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0cde) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0cdf) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0ce0) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0ce2) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0ce6) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0cf0) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0d05) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0d0d) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0d0e) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0d11) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0d12) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0d29) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0d2a) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0d3a) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0d60) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0d62) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0d66) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0d70) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0e01) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0e2f) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0e30) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0e31) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0e32) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0e34) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0e40) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0e46) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0e50) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0e5a) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0e81) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0e83) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0e84) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0e85) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0e87) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0e89) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0e8a) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0e8b) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0e8d) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0e8e) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0e94) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0e98) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0e99) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0ea0) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0ea1) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0ea4) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0ea5) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0ea6) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0ea7) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0ea8) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0eaa) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0eac) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0ead) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0eaf) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0eb0) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0eb1) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0eb2) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0eb4) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0ebd) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0ebe) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0ec0) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0ec5) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0ed0) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0eda) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0f20) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0f2a) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0f40) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0f48) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0f49) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x0f6a) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x10a0) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x10c6) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x10d0) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x10f7) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1100) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1101) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1102) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1104) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1105) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1108) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1109) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x110a) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x110b) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x110d) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x110e) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1113) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x113c) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x113d) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x113e) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x113f) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1140) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1141) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x114c) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x114d) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x114e) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x114f) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1150) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1151) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1154) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1156) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1159) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x115a) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x115f) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1162) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1163) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1164) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1165) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1166) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1167) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1168) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1169) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x116a) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x116d) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x116f) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1172) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1174) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1175) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1176) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x119e) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x119f) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x11a8) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x11a9) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x11ab) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x11ac) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x11ae) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x11b0) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x11b7) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x11b9) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x11ba) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x11bb) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x11bc) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x11c3) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x11eb) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x11ec) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x11f0) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x11f1) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x11f9) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x11fa) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1e00) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1e9c) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1ea0) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1efa) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1f00) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1f16) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1f18) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1f1e) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1f20) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1f46) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1f48) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1f4e) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1f50) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1f58) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1f59) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1f5a) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1f5b) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1f5c) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1f5d) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1f5e) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1f5f) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1f7e) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1f80) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1fb5) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1fb6) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1fbd) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1fbe) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1fbf) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1fc2) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1fc5) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1fc6) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1fcd) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1fd0) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1fd4) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1fd6) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1fdc) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1fe0) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1fed) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1ff2) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1ff5) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1ff6) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x1ffd) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x2126) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x2127) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x212a) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x212c) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x212e) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x212f) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x2180) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x2183) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x3007) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x3008) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x3021) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x302a) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x3041) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x3095) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x30a1) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x30fb) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x3105) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x312d) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x4e00) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x9fa6) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0xac00) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0xd7a4) {
			if (!Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		while (c < 0x10000) {
			if (Verifier.isXMLLetterOrDigit((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLLetterOrDigit but it did not.");
			}
			c++;
		}

		System.out.printf("Completed test testIsXMLLetterOrDigit in %dms\n", System.currentTimeMillis() - ms);
	}

	// Automated test built by VerifierTestBuilder
	@Test
	public void testIsXMLWhitespace() {
		int c = 0;
		long ms = System.currentTimeMillis();
		while (c < 0x0009) {
			if (Verifier.isXMLWhitespace((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLWhitespace but it did not.");
			}
			c++;
		}

		while (c < 0x000b) {
			if (!Verifier.isXMLWhitespace((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLWhitespace but it did not.");
			}
			c++;
		}

		while (c < 0x000d) {
			if (Verifier.isXMLWhitespace((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLWhitespace but it did not.");
			}
			c++;
		}

		while (c < 0x000e) {
			if (!Verifier.isXMLWhitespace((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLWhitespace but it did not.");
			}
			c++;
		}

		while (c < 0x0020) {
			if (Verifier.isXMLWhitespace((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLWhitespace but it did not.");
			}
			c++;
		}

		while (c < 0x0021) {
			if (!Verifier.isXMLWhitespace((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to pass isXMLWhitespace but it did not.");
			}
			c++;
		}

		while (c < 0x10000) {
			if (Verifier.isXMLWhitespace((char)c)) {
				fail("Expected char 0x" + Integer.toHexString(c) + " to fail isXMLWhitespace but it did not.");
			}
			c++;
		}

		System.out.printf("Completed test testIsXMLWhitespace in %dms\n", System.currentTimeMillis() - ms);
	}


}
