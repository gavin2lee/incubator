package com.lachesis.mnis.core.identity.entity;

import java.nio.ByteBuffer;
import java.util.Date;

public class UserFinger {
	public static final String MODIFY_TYPE_CREATE = "C";
	public static final String MODIFY_TYPE_UPDATE = "U";
	public static final String MODIFY_TYPE_DELETE = "D";

	private static final int PACKED_LENGTH = 1112;
	private static final int UNPACKED_LENGTH = 1024;
	private static final int SEG_NO = 8;
	private static final short HEADER = (short) 0XEF01;
	private static final int ADDR = 0XFFFFFFFF;
	private static final byte PID = 0X02;
	private static final byte PID_END = 0X08;
	private static final short LENGTH = (short) 130;// 128+2
	private static final byte[] prefix;
	static {
		prefix = ByteBuffer.allocate(9).putShort(HEADER).putInt(ADDR).put(PID)
				.putShort(LENGTH).array();
	}

	private int id;
	private String userCode;
	private String deptCode;
	private String feature;
	private String secretKey;
	private Date createDate;
	private String modifyType; 
	/**
	 * 将原始特征字节流包装成1112字节的数据流
	 * 
	 * @return 原始字节流为null或长度不为1024时返回null
	 */
	public byte[] packRawFeature(byte[] data) {
		if (data == null || data.length != UNPACKED_LENGTH) {
			return null;
		}
		ByteBuffer target = ByteBuffer.allocate(PACKED_LENGTH);

		int SEG_LENGH = UNPACKED_LENGTH / SEG_NO;
		ByteBuffer src = ByteBuffer.wrap(data);

		byte[] packetData = new byte[SEG_LENGH];
		int sum0 = PID + (LENGTH & 0xFF00) + (LENGTH & 0xFF);
		// 包标示、包长度和包内容的所有字节的算术累计和，超过2 字节的进位忽略。传送时高字节在前。
		for (int i = 0; i < SEG_NO; i++) {
			src.get(packetData, 0, SEG_LENGH);
			int sum = sum0;
			if (i == SEG_NO - 1) {
				sum += PID_END - PID;
			}
			// byte默认是有符号的，Java转换成int时会提升符号位，&0xff将byte值无差异转成int
			for (byte d : packetData) {
				sum += d & 0xFF;
			}
			target.put(prefix).put(packetData).put((byte) (sum >> 8))
					.put((byte) sum);// 9+128+2=139个字节
		}
		byte[] targetArray = target.array();
		// 最后一个数据包的PID用结束标志,
		targetArray[targetArray.length - 1 - LENGTH - 2] = PID_END;
		// 相应调整最后一个数据包的校验和:02变为08即加6
		return targetArray;
	}

	/**
	 * 将打包的字节流（1112字节）还原成原始字节（1024字节）
	 * 
	 * @param packedFeature
	 * @return
	 */
	public byte[] unpackFeature(byte[] packedFeature) {
		if (packedFeature == null || packedFeature.length != PACKED_LENGTH) {
			throw new IllegalArgumentException("特征字节流包为空或长度无效！");
		}
		byte[] rawFeature = new byte[1024];

		int segLength = PACKED_LENGTH / SEG_NO;
		int dataLength = 128;
		for (int i = 0; i < SEG_NO; i++) {
			int t = 0;
			int segStart = i * segLength + 9;
			for (int j = segStart; j < segStart + dataLength; j++) {
				rawFeature[i * dataLength + t++] = packedFeature[j];
			}
		}

		return rawFeature;
	}

	public static boolean isPacked(byte[] packedFeature) {
		if (packedFeature == null) {
			throw new IllegalArgumentException("特征字节流包为空！");
		}
		int len = packedFeature.length;
		if (len != PACKED_LENGTH && len != UNPACKED_LENGTH) {
			throw new IllegalArgumentException("特征字节流包长度无效！");
		}
		return len == PACKED_LENGTH ? true : false;
	}

	/**
	 * 1024字节
	 * 
	 * @return
	 */
	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getModifyType() {
		return modifyType;
	}

	public void setModifyType(String modifyType) {
		this.modifyType = modifyType;
	}
	
}
