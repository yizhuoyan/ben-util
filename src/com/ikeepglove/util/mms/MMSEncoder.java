/*
 * @(#)MMEncoder.java	1.1
 *
 * Copyright (c) Nokia Corporation 2002
 *
 */

package com.ikeepglove.util.mms;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * The MMEncoder class encodes Multimedia Message object (MMMessage) into an
 * array of bytes according to the specification WAP-209-MMSEncapsulation (WAP
 * Forum).
 * 
 */

public class MMSEncoder implements MMSConstants {

	private boolean multipartRelated;
	private ByteArrayOutputStream out;

	public MMSEncoder() {
		super();
		reset();
	}

	/**
	 * Resets the Decoder object.
	 * 
	 */
	public void reset() {
		multipartRelated = false;
		out = null;
	}


	/**
	 * Encode known content type assignments. List of the content type
	 * assignments can be found from WAP-203-WSP, Table 40 This version is
	 * compliant with Approved version 4-May-2000
	 * 
	 * @return assigned number
	 */

	private byte encodeContentType(String sContentType) {
		if (sContentType.equalsIgnoreCase("*/*"))
			return 0x00;
		else if (sContentType.equalsIgnoreCase("text/*"))
			return 0x01;
		else if (sContentType.equalsIgnoreCase("text/html"))
			return 0x02;
		else if (sContentType.equalsIgnoreCase("text/plain"))
			return 0x03;
		else if (sContentType.equalsIgnoreCase("text/x-hdml"))
			return 0x04;
		else if (sContentType.equalsIgnoreCase("text/x-ttml"))
			return 0x05;
		else if (sContentType.equalsIgnoreCase("text/x-vCalendar"))
			return 0x06;
		else if (sContentType.equalsIgnoreCase("text/x-vCard"))
			return 0x07;
		else if (sContentType.equalsIgnoreCase("text/vnd.wap.wml"))
			return 0x08;
		else if (sContentType.equalsIgnoreCase("text/vnd.wap.wmlscript"))
			return 0x09;
		else if (sContentType.equalsIgnoreCase("text/vnd.wap.channel"))
			return 0x0A;
		else if (sContentType.equalsIgnoreCase("multipart/*"))
			return 0x0B;
		else if (sContentType.equalsIgnoreCase("multipart/mixed"))
			return 0x0C;
		else if (sContentType.equalsIgnoreCase("multipart/form-data"))
			return 0x0D;
		else if (sContentType.equalsIgnoreCase("multipart/byteranges"))
			return 0x0E;
		else if (sContentType.equalsIgnoreCase("multipart/alternative"))
			return 0x0F;
		else if (sContentType.equalsIgnoreCase("application/*"))
			return 0x10;
		else if (sContentType.equalsIgnoreCase("application/java-vm"))
			return 0x11;
		else if (sContentType
				.equalsIgnoreCase("application/x-www-form-urlencoded"))
			return 0x12;
		else if (sContentType.equalsIgnoreCase("application/x-hdmlc"))
			return 0x13;
		else if (sContentType.equalsIgnoreCase("application/vnd.wap.wmlc"))
			return 0x14;
		else if (sContentType
				.equalsIgnoreCase("application/vnd.wap.wmlscriptc"))
			return 0x15;
		else if (sContentType.equalsIgnoreCase("application/vnd.wap.channelc"))
			return 0x16;
		else if (sContentType.equalsIgnoreCase("application/vnd.wap.uaprof"))
			return 0x17;
		else if (sContentType
				.equalsIgnoreCase("application/vnd.wap.wtls-ca-certificate"))
			return 0x18;
		else if (sContentType
				.equalsIgnoreCase("application/vnd.wap.wtls-user-certificate"))
			return 0x19;
		else if (sContentType.equalsIgnoreCase("application/x-x509-ca-cert"))
			return 0x1A;
		else if (sContentType.equalsIgnoreCase("application/x-x509-user-cert"))
			return 0x1B;
		else if (sContentType.equalsIgnoreCase("image/*"))
			return 0x1C;
		else if (sContentType.equalsIgnoreCase("image/gif"))
			return 0x1D;
		else if (sContentType.equalsIgnoreCase("image/jpeg"))
			return 0x1E;
		else if (sContentType.equalsIgnoreCase("image/tiff"))
			return 0x1F;
		else if (sContentType.equalsIgnoreCase("image/png"))
			return 0x20;
		else if (sContentType.equalsIgnoreCase("image/vnd.wap.wbmp"))
			return 0x21;
		else if (sContentType
				.equalsIgnoreCase("application/vnd.wap.multipart.*"))
			return 0x22;
		else if (sContentType
				.equalsIgnoreCase("application/vnd.wap.multipart.mixed"))
			return 0x23;
		else if (sContentType
				.equalsIgnoreCase("application/vnd.wap.multipart.form-data"))
			return 0x24;
		else if (sContentType
				.equalsIgnoreCase("application/vnd.wap.multipart.byteranges"))
			return 0x25;
		else if (sContentType
				.equalsIgnoreCase("application/vnd.wap.multipart.alternative"))
			return 0x26;
		else if (sContentType.equalsIgnoreCase("application/xml"))
			return 0x27;
		else if (sContentType.equalsIgnoreCase("text/xml"))
			return 0x28;
		else if (sContentType.equalsIgnoreCase("application/vnd.wap.wbxml"))
			return 0x29;
		else if (sContentType.equalsIgnoreCase("application/x-x968-cross-cert"))
			return 0x2A;
		else if (sContentType.equalsIgnoreCase("application/x-x968-ca-cert"))
			return 0x2B;
		else if (sContentType.equalsIgnoreCase("application/x-x968-user-cert"))
			return 0x2C;
		else if (sContentType.equalsIgnoreCase("text/vnd.wap.si"))
			return 0x2D;
		else if (sContentType.equalsIgnoreCase("application/vnd.wap.sic"))
			return 0x2E;
		else if (sContentType.equalsIgnoreCase("text/vnd.wap.sl"))
			return 0x2F;
		else if (sContentType.equalsIgnoreCase("application/vnd.wap.slc"))
			return 0x30;
		else if (sContentType.equalsIgnoreCase("text/vnd.wap.co"))
			return 0x31;
		else if (sContentType.equalsIgnoreCase("application/vnd.wap.coc"))
			return 0x32;
		else if (sContentType
				.equalsIgnoreCase("application/vnd.wap.multipart.related"))
			return 0x33;
		else if (sContentType.equalsIgnoreCase("application/vnd.wap.sia"))
			return 0x34;
		else if (sContentType.equalsIgnoreCase("text/vnd.wap.connectivity-xml"))
			return 0x35;
		else if (sContentType
				.equalsIgnoreCase("application/vnd.wap.connectivity-wbxml"))
			return 0x36;
		else
			return 0;
	}

	private int unsignedByte(byte value) {
		if (value < 0) {
			return (value + 256);
		} else {
			return value;
		}
	}

	private void writeValueLength(long value) {

		if (value <= 30)
			out.write((int) value);
		else {
			out.write(31);
			int data[] = EncodeUintvarNumber(value);
			int numValue;
			for (int i = 1; i <= data[0]; i++) {
				numValue = data[i];
				out.write(numValue);
			}
		}
	}

	private void writeUintvar(long value) {
		int data[] = EncodeUintvarNumber(value);
		int numValue;
		for (int i = 1; i <= data[0]; i++) {
			numValue = data[i];
			out.write(numValue);
		}
	}

	/**
	 * Encodes the Multimedia Message set by calling setMessage(MMMessage msg)
	 */
	public  byte[] encodeMessage(MMS msg) throws MMSException {
		if (null==msg)
			throw new MMSException("No Multimedia Messages in the encoder");
		int numValue;
		String strValue;
		multipartRelated = false;
		try {
			out = new ByteArrayOutputStream();

			if (!msg.isMessageTypeAvailable()) {
				out.close();
				throw new MMSException(
						"Invalid Multimedia Message format.");
			}

			byte nMessageType = msg.getMessageType();

			switch (nMessageType) {
			default: {
				out.close();
				throw new MMSException(
						"Invalid Multimedia Message format.");
			}

			case MESSAGE_TYPE_M_DELIVERY_IND: // ----------------------------
												// m-delivery-ind

				// ------------------- MESSAGE TYPE --------
				out.write(FN_MESSAGE_TYPE + 0x80);
				out.write(nMessageType);

				// ------------------- MESSAGE ID ------
				if (msg.isMessageIdAvailable()) {
					out.write(FN_MESSAGE_ID + 0x80);
					out.write(msg.getMessageId().getBytes());
					out.write(0x00);
				} else {
					out.close();
					throw new MMSException(
							"The field Message-ID of the Multimedia Message is null");
				}

				// ------------------- VERSION -------------
				out.write(FN_MMS_VERSION + 0x80);
				if (!msg.isVersionAvailable()) {
					numValue = MMS_VERSION_10;
				} else {
					numValue = msg.getVersion();
				}
				out.write(numValue);

				// ------------------- DATE ----------------
				if (msg.isDateAvailable()) {
					long secs = (msg.getDate()).getTime() / 1000;
					int data[] = EncodeMultiByteNumber(secs);
					if (data == null) {
						out.close();
						throw new MMSException(
								"An error occurred encoding the sending date of the Multimedia Message");
					}
					out.write(FN_DATE + 0x80);
					int nCount = data[0];
					out.write(nCount);
					for (int i = 1; i <= nCount; i++) {
						out.write(data[i]);
					}
				}

				// ------------------- TO ------------------
				if (msg.isToAvailable()) {
					List<MMSAddress> sAddress = msg.getTo();
					if (sAddress == null) {
						out.close();
						throw new MMSException(
								"The field TO of the Multimedia Message is set to null.");
					}
					for (MMSAddress address:sAddress) {
						strValue = address.getFullAddress();
						if (strValue != null) {
							out.write(FN_TO + 0x80);
							out.write(strValue.getBytes());
							out.write(0x00);
						}
					}
				} else {
					out.close();
					throw new MMSException(
							"No recipient specified in the Multimedia Message.");
				}

				// ------------------- MESSAGE-STATUS ----------------
				if (msg.isStatusAvailable()) {
					out.write(FN_STATUS + 0x80);
					out.write(msg.getMessageStatus());
				} else {
					out.close();
					throw new MMSException(
							"The field Message-ID of the Multimedia Message is null");
				}

				break;

			case MESSAGE_TYPE_M_SEND_REQ: // ----------------------------
											// m-send-req

				// ------------------- MESSAGE TYPE --------
				out.write(FN_MESSAGE_TYPE + 0x80);
				out.write(nMessageType);

				// ------------------- TRANSACTION ID ------
				if (msg.isTransactionIdAvailable()) {
					out.write(FN_TRANSACTION_ID + 0x80);
					out.write(msg.getTransactionId().getBytes());
					out.write(0x00);
				}

				// ------------------- VERSION -------------
				out.write(FN_MMS_VERSION + 0x80);
				if (!msg.isVersionAvailable()) {
					numValue = MMS_VERSION_10;
				} else {
					numValue = msg.getVersion();
				}
				out.write(numValue);

				// ------------------- DATE ----------------
				if (msg.isDateAvailable()) {
					long secs = (msg.getDate()).getTime() / 1000;
					int data[] = EncodeMultiByteNumber(secs);
					if (data == null) {
						out.close();
						throw new MMSException(
								"An error occurred encoding the sending date of the Multimedia Message");
					}
					out.write(FN_DATE + 0x80);
					int nCount = data[0];
					out.write(nCount);
					for (int i = 1; i <= nCount; i++) {
						out.write(data[i]);
					}
				}

				// ------------------- FROM ----------------
				if (msg.isFromAvailable()) {
					out.write(FN_FROM + 0x80);

					strValue = (msg.getFrom()).getFullAddress();
					if (strValue == null) {
						out.close();
						throw new MMSException(
								"The field from is assigned to null");
					}

					// Value-length
					writeValueLength(strValue.length() + 2);
					// Address-present-token
					out.write(0x80);

					// Encoded-string-value
					out.write(strValue.getBytes());
					out.write(0x00);
				} else {
					// Value-length
					out.write(1);
					out.write(0x81);
				}

				// ------------------- TO ------------------
				if (msg.isToAvailable()) {
					List<MMSAddress> sAddress = msg.getTo();
					if (sAddress == null) {
						out.close();
						throw new MMSException(
								"The field TO of the Multimedia Message is set to null.");
					}
					for (MMSAddress address:sAddress) {
						strValue = address.getFullAddress();
						if (strValue != null) {
							out.write(FN_TO + 0x80);
							out.write(strValue.getBytes());
							out.write(0x00);
						}
					}
				}

				// ------------------- CC ------------------
				if (msg.isCcAvailable()) {
					List<MMSAddress> sAddress = msg.getCc();
					if (sAddress == null) {
						out.close();
						throw new MMSException(
								"The field CC of the Multimedia Message is set to null.");
					}

					for (MMSAddress address:sAddress) {
						strValue = address.getFullAddress();
						if (strValue != null) {
							out.write(FN_CC + 0x80);
							out.write(strValue.getBytes());
							out.write(0x00);
						}
					}
				}

				// ------------------- BCC ------------------
				if (msg.isBccAvailable()) {
					List<MMSAddress> sAddress = msg.getBcc();
					if (sAddress == null) {
						out.close();
						throw new MMSException(
								"The field BCC of the Multimedia Message is set to null.");
					}

					for (MMSAddress address:sAddress) {
						strValue =address.getFullAddress();
						if (strValue != null) {
							out.write(FN_BCC + 0x80);
							out.write(strValue.getBytes());
							out.write(0x00);
						}
					}
				}

				if (!(msg.isToAvailable() || msg.isCcAvailable() || msg
						.isBccAvailable())) {
					out.close();
					throw new MMSException(
							"No recipient specified in the Multimedia Message.");
				}

				// ---------------- SUBJECT --------------
				if (msg.isSubjectAvailable()) {
					out.write(FN_SUBJECT + 0x80);
					// m_Out.write(m_Message.getSubject().getBytes());
					//采用UTF-8编码
					out.write(msg.getSubject().getBytes("utf-8").length + 2);
					out.write(0xEA);
					out.write(msg.getSubject().getBytes("utf-8"));
					out.write(0x00);
				}

				// ------------------- DELIVERY-REPORT ----------------
				if (msg.isDeliveryReportAvailable()) {
					out.write(FN_DELIVERY_REPORT + 0x80);
					if (msg.getDeliveryReport() == true)
						out.write(0x80);
					else
						out.write(0x81);
				}

				// ------------------- SENDER-VISIBILITY ----------------
				if (msg.isSenderVisibilityAvailable()) {
					out.write(FN_SENDER_VISIBILITY + 0x80);
					out.write(msg.getSenderVisibility());
				}

				// ------------------- READ-REPLY ----------------
				if (msg.isReadReplyAvailable()) {
					out.write(FN_READ_REPLY + 0x80);
					if (msg.getReadReply() == true)
						out.write(0x80);
					else
						out.write(0x81);
				}

				// ---------------- MESSAGE CLASS ---------
				if (msg.isMessageClassAvailable()) {
					out.write(FN_MESSAGE_CLASS + 0x80);
					out.write(msg.getMessageClass());
				}

				// ---------------- EXPIRY ----------------
				if (msg.isExpiryAvailable()) {
					long secs = (msg.getExpiry()).getTime() / 1000;
					int data[] = EncodeMultiByteNumber(secs);
					if (data == null) {
						out.close();
						throw new MMSException(
								"An error occurred encoding the EXPIRY field of the Multimedia Message. The field is set to null");
					}
					int nCount = data[0];

					out.write(FN_EXPIRY + 0x80);

					// Value-length
					writeValueLength(nCount + 2);

					if (msg.isExpiryAbsolute()) {
						// Absolute-token
						out.write(0x80);
					} else {
						// Relative-token
						out.write(0x81);
					}

					// Date-value or Delta-seconds-value
					for (int i = 0; i <= nCount; i++) {
						out.write(data[i]);
					}
				}

				// ---------------- DELIVERY TIME ----------------
				if (msg.isDeliveryTimeAvailable()) {
					long secs = (msg.getDeliveryTime()).getTime() / 1000;
					int data[] = EncodeMultiByteNumber(secs);
					if (data == null) {
						out.close();
						throw new MMSException(
								"The field DELIVERY TIME of the Multimedia Message is set to null.");
					}
					int nCount = data[0];

					out.write(FN_DELIVERY_TIME + 0x80);

					// Value-length
					writeValueLength(nCount + 2);

					if (msg.isDeliveryTimeAbsolute()) {
						// Absolute-token
						out.write(0x80);
					} else {
						// Relative-token
						out.write(0x81);
					}

					// Date-value or Delta-seconds-value
					for (int i = 0; i <= nCount; i++) {
						out.write(data[i]);
					}
				}

				// ---------------- PRIORITY ----------------
				if (msg.isPriorityAvailable()) {
					out.write(FN_PRIORITY + 0x80);
					out.write(msg.getPriority());
				}

				// ---------------- CONTENT TYPE ----------------
				if (msg.isContentTypeAvailable()) {
					multipartRelated = false;
					out.write(FN_CONTENT_TYPE + 0x80);

					byte ctype = encodeContentType(msg.getContentType());

					if (ctype == 0x33) {
						// application/vnd.wap.multipart.related
						multipartRelated = true;
						int valueLength = 1;
						String mprt = msg.getMultipartRelatedType();
						valueLength += mprt.length() + 2;
						String start = msg.getPresentationId();
						valueLength += start.length() + 2;
						// Value-length
						writeValueLength(valueLength);
						// Well-known-media
						out.write(0x33 + 0x80);
						// Parameters
						// Type
						out.write(0x09 + 0x80);
						out.write(mprt.getBytes());
						out.write(0x00);
						// Start
						out.write(0x0A + 0x80);
						out.write(start.getBytes());
						out.write(0x00);
					} else {
						if (ctype > 0x00)
							out.write(ctype + 0x80);
						else {
							out.write(msg.getContentType().getBytes());
							out.write(0x00);
						}
					}
				} else {
					out.close();
					throw new MMSException(
							"The field CONTENT TYPE of the Multimedia Message is not specified.");
				}

				// -------------------------- BODY -------------
				int nPartsCount = msg.getNumContents();
				out.write(nPartsCount);
				List<MMSContent> parts = msg.getContentList();
				for (MMSContent part:parts) {
					boolean bRetVal = EncodePart(part);
					if (!bRetVal) {
						out.close();
						throw new MMSException(
								"The entry having Content-id = "
										+ part.getContentId()
										+ " cannot be encoded.");
					}
				}

				break;
			}

			out.close();
		} catch (IOException e) {
			throw new MMSException(
					"An IO error occurred encoding the Multimedia Message.");
		}
		return out.toByteArray();
	}

	private int[] EncodeMultiByteNumber(long lData) {

		int data[] = new int[32];
		long lDivider = 1L;
		int nSize = 0;
		long lNumber = lData;

		for (int i = 0; i < 32; i++)
			data[i] = 0;

		for (int i = 4; i >= 0; i--) {
			lDivider = 1L;
			for (int j = 0; j < i; j++)
				lDivider *= 256L;

			int q = (int) (lNumber / lDivider);

			if (q != 0 || nSize != 0) {
				int r = (int) (lNumber % lDivider);
				data[nSize + 1] = q;
				lNumber = r;
				nSize++;
			}
		}

		data[0] = nSize;
		return data;
	}

	private int[] EncodeUintvarNumber(long lData) {
		int data[] = new int[32];
		long lDivider = 1L;
		int nSize = 0;
		long lNumber = lData;

		for (int i = 0; i < 32; i++)
			data[i] = 0;

		for (int i = 4; i >= 0; i--) {
			lDivider = 1L;
			for (int j = 0; j < i; j++)
				lDivider *= 128L;

			int q = (int) (lNumber / lDivider);
			if (q != 0 || nSize != 0) {
				int r = (int) (lNumber % lDivider);
				data[nSize + 1] = q;
				if (i != 0)
					data[nSize + 1] += 128;
				lNumber = r;
				nSize++;
			}
		}

		data[0] = nSize;
		return data;
	}

	private boolean EncodePart(MMSContent part) throws IOException {

		if (part == null)
			return false;

		int nHeadersLen = 0; // nHeadersLen = nLengthOfContentType +
								// nLengthOfHeaders
		int nContentType = 0;

		int nLengthOfHeaders = 0;
		int nLengthOfContentType = 0;

		// -------- HeadersLen = ContentType + Headers fields ---------
		if ((part.getContentId().length() > 0) && (multipartRelated)) {
			if (part.getContentId().getBytes()[0] == '<') {
				nLengthOfHeaders = 2 + (part.getContentId()).length() + 1;
				// 2 = 0xC0 (Content-ID) + 0x22 (quotes)
				// 1 = 0x00 (at the end of the contentID)
			} else {
				nLengthOfHeaders = 1 + (part.getContentId()).length() + 1;
				// 1 = 0x8E (Content-Location)
				// 1 = 0x00 (end string)
			}
		}

		// -------- DataLen -------------
		long lDataLen = part.getLength();

		// -------- ContentType ---------
		nContentType = encodeContentType(part.getType()) + 128;

		if (nContentType > 0x80) {
			// ---------- Well Known Content Types
			// ------------------------------
			if (nContentType == 0x83) { // text/plain
				nLengthOfContentType = 4;
				// 4 = 0x03 (Value Length)+ 0x83(text/plain) + 0x81 (Charset) +
				// 0x83 (us-ascii code)

				nHeadersLen = nLengthOfContentType + nLengthOfHeaders;

				// write HeadersLen
				writeUintvar(nHeadersLen);

				// write DataLen
				writeUintvar(lDataLen);

				// write ContentType
				out.write(0x03); // length of content type
				out.write(nContentType);
				out.write(0x81); // charset parameter
				// m_Out.write(0x83); // us-ascii code
				out.write(0xEA); // utf-8
			} else {
				nLengthOfContentType = 1;
				nHeadersLen = nLengthOfContentType + nLengthOfHeaders;
				// write HeadersLen
				writeUintvar(nHeadersLen);
				// write DataLen
				writeUintvar(lDataLen);
				// write ContentType
				out.write(nContentType);
			}
		} else {
			// ----------- Don't known Content Type
			if (part.getType().equalsIgnoreCase(CT_APPLICATION_SMIL)) {
				nLengthOfContentType = 1 + part.getType().length() + 3;
				// 1 = 0x13 (Value Length)
				// 3 = 0x00 + 0x81 (Charset) + 0x83 (us-ascii code)

				nHeadersLen = nLengthOfContentType + nLengthOfHeaders;

				// write HeadersLen
				writeUintvar(nHeadersLen);
				// write DataLen
				writeUintvar(lDataLen);

				// write ContentType
				out.write(0x13); // 13 characters, actually
									// part.getType().length()+1+1+1
				out.write(part.getType().getBytes());
				out.write(0x00);
				out.write(0x81); // charset parameter
				// m_Out.write(0x83); // ascii-code
				out.write(0xEA); // utf-8
			} else {
				nLengthOfContentType = part.getType().length() + 1;
				// 1 = 0x00

				nHeadersLen = nLengthOfContentType + nLengthOfHeaders;

				// write HeadersLen
				writeUintvar(nHeadersLen);
				// write DataLen
				writeUintvar(lDataLen);
				// write ContentType
				out.write(part.getType().getBytes());
				out.write(0x00);
			}
		}

		// writes the Content ID or the Content Location
		if ((part.getContentId().length() > 0) && (multipartRelated)) {
			if (part.getContentId().charAt(0) == '<') {
				out.write(0xC0);
				out.write(0x22);
				out.write(part.getContentId().getBytes());
				out.write(0x00);
			} else {
				// content id
				out.write(0x8E);
				out.write(part.getContentId().getBytes());
				out.write(0x00);
			}
		}

		// ----------- Data --------------
		byte data[] = part.getContent();
		out.write(data);

		return true;
	}

}
