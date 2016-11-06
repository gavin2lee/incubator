/*
 *    Copyright 2012-2013 The Haohui Network Corporation
 */
package com.harmazing.framework.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOException;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;

/**
 * 图片压缩工具类 提供的方法中可以设定生成的 缩略图片的大小尺寸、压缩尺寸的比例、图片的质量等
 *
 * <pre>
 *  调用示例：
 * resiz(srcImg, tarDir + "car_1_maxLength_11-220px-hui.jpg", 220, 0.7F);
 * </pre>
 *
 */
public class ImageUtil {
	public static void changeQualityImage(String srcFile, String outFile,
			float quality) {
		// 得到图片
		BufferedImage src = inputImage(srcFile);
		int old_w = src.getWidth();
		// 得到源图宽
		int old_h = src.getHeight();
		// 新图的长
		BufferedImage tempImg = new BufferedImage(old_w, old_h,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = tempImg.createGraphics();
		g.setColor(Color.white);
		// 从原图上取颜色绘制新图
		g.fillRect(0, 0, old_w, old_h);
		g.drawImage(src, 0, 0, old_w, old_h, Color.white, null);
		g.dispose();
		BufferedImage newImg = new BufferedImage(old_w, old_h,
				BufferedImage.TYPE_INT_RGB);
		newImg.getGraphics().drawImage(
				tempImg.getScaledInstance(old_w, old_h, Image.SCALE_SMOOTH), 0,
				0, null);
		// 调用方法输出图片文件
		outImage(outFile, newImg, quality);
	}

	public static void resizeImage(String srcFile, String outFile, int width,
			int height, float quality, int outType) {
		// 得到图片
		BufferedImage src = null;
		try {
			// src = ImageIO.read(new File(srcFile));
			src = inputImage(srcFile);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		int old_w = src.getWidth();
		// 得到源图宽
		int old_h = src.getHeight();
		// 得到源图长
		int new_w = 0;
		// 新图的宽
		int new_h = 0;

		// 根据图片尺寸压缩比得到新图的尺寸
		int offset_w = 0;
		int offset_h = 0;
		if (width >= height) {
			if (((float) width / height) >= ((float) old_w / old_h)) {
				// 补白区域为左右
				new_h = height;
				new_w = Math.round(height * ((float) old_w / old_h));
				offset_h = 0;
				offset_w = Math.round((width - new_w) / 2);
			} else {
				// 补白区域为上下
				new_w = width;
				new_h = Math.round(width * ((float) old_h / old_w));
				offset_w = 0;
				offset_h = Math.round((height - new_h) / 2);
			}
		} else {
			if (((float) height / height) >= ((float) old_h / old_w)) {
				// 补白区域为上下
				new_w = width;
				new_h = Math.round(width * ((float) old_h / old_w));
				offset_w = 0;
				offset_h = Math.round((height - new_h) / 2);
			} else {
				// 补白区域为左右
				new_h = height;
				new_w = Math.round(height * ((float) old_w / old_h));
				offset_h = 0;
				offset_w = Math.round((width - new_w) / 2);
			}
		}
		if (outType == 1) {
			BufferedImage newImg = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics graphics = newImg.getGraphics();
			graphics.setColor(Color.white);
			graphics.fillRect(0, 0, width, height);
			newImg.getGraphics().drawImage(
					src.getScaledInstance(new_w, new_h, Image.SCALE_SMOOTH),
					offset_w, offset_h, null);
			outImage(outFile, newImg, quality);
		}

		if (outType == 2) {
			BufferedImage newImg = null;
			Graphics2D graphics = null;
			if (quality > 0.6f) {
				newImg = new BufferedImage(width, height,
						Transparency.TRANSLUCENT);
				graphics = newImg.createGraphics();
				graphics.getDeviceConfiguration().createCompatibleImage(width,
						height, Transparency.TRANSLUCENT);
			} else {
				newImg = new BufferedImage(width, height,
						BufferedImage.TRANSLUCENT);
				graphics = newImg.createGraphics();
				graphics.getDeviceConfiguration().createCompatibleImage(width,
						height, Transparency.TRANSLUCENT);
			}
			newImg.getGraphics().drawImage(
					src.getScaledInstance(new_w, new_h, Image.SCALE_SMOOTH),
					offset_w, offset_h, null);
			try {
				ImageIO.write(newImg, "png", new File(outFile));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * 图片文件读取
	 *
	 * @param srcImgPath
	 * @return
	 */
	public static BufferedImage inputImage(String srcImgPath)
			throws RuntimeException {
		// 创建图片.
		BufferedImage image = null;
		ImageInputStream iis = null;
		try {
			// Create an image input stream on the image
			File srcFile = new File(srcImgPath);
			iis = ImageIO.createImageInputStream(srcFile);
			// Find all image readers that recognize the image format
			Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
			if (!iter.hasNext()) {
				throw new RuntimeException("读取图片文件出错！");
			}
			// Use the first reader
			ImageReader reader = iter.next();

			// 设置input.
			ImageInputStream input = ImageIO.createImageInputStream(srcFile);
			reader.setInput(input);

			// 创建图片.
			try {
				// 尝试读取图片 (包括颜色的转换).
				image = reader.read(0);
			} catch (IIOException e) {
				if ("JPEG".equalsIgnoreCase(reader.getFormatName())) {
					// 读取Raster (没有颜色的转换).
					Raster raster = reader.readRaster(0, null);
					// 随意选择一个BufferedImage类型.
					int imageType;
					switch (raster.getNumBands()) {
					case 1:
						imageType = BufferedImage.TYPE_BYTE_GRAY;
						break;
					case 3:
						imageType = BufferedImage.TYPE_3BYTE_BGR;
						break;
					case 4:
						imageType = BufferedImage.TYPE_4BYTE_ABGR;
						break;
					default:
						throw new UnsupportedOperationException();
					}
					// 创建一个BufferedImage.
					image = new BufferedImage(raster.getWidth(),
							raster.getHeight(), imageType);
					// 设置图片数据.
					image.getRaster().setRect(raster);
				} else {
					throw new UnsupportedOperationException();
				}
			}
		} catch (Exception e1) {
			// e1.printStackTrace();
			throw new RuntimeException("读取图片文件出错！", e1);
		} finally {
			if (iis != null) {
				// Close stream
				try {
					iis.close();
				} catch (IOException e) {
					// e.printStackTrace();
				}
			}
		}
		return image;
	}

	public static String getFormatName(File f) {
		ImageInputStream iis = null;
		try {
			// Create an image input stream on the image
			iis = ImageIO.createImageInputStream(f);
			// Find all image readers that recognize the image format
			Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
			if (!iter.hasNext()) {
				// No readers found
				return "";
			}
			// Use the first reader
			ImageReader reader = iter.next();
			// Return the format name
			return reader.getFormatName();
		} catch (IOException e) {
			//
			throw new RuntimeException("读取图片文件出错！", e);
		} finally {
			if (iis != null) {
				// Close stream
				try {
					iis.close();
				} catch (IOException e) {
					// e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 将图片按照指定的图片尺寸、源图片质量压缩(默认质量为1)
	 *
	 * @param srcImgPath
	 *            :源图片路径
	 * @param outImgPath
	 *            :输出的压缩图片的路径
	 * @param new_w
	 *            :压缩后的图片宽
	 * @param new_h
	 *            :压缩后的图片高
	 */
	public static void resize(String srcImgPath, String outImgPath, int new_w,
			int new_h) {
		resize(srcImgPath, outImgPath, new_w, new_h, 1F);
	}

	/**
	 * 将图片按照指定的尺寸比例、源图片质量压缩(默认质量为1)
	 *
	 * @param srcImgPath
	 *            :源图片路径
	 * @param outImgPath
	 *            :输出的压缩图片的路径
	 * @param ratio
	 *            :压缩后的图片尺寸比例
	 * @param per
	 *            :百分比
	 */
	public static void resize(String srcImgPath, String outImgPath, float ratio) {
		resize(srcImgPath, outImgPath, ratio, 1F);
	}

	/**
	 * 将图片按照指定长或者宽的最大值来压缩图片(默认质量为1)
	 *
	 * @param srcImgPath
	 *            :源图片路径
	 * @param outImgPath
	 *            :输出的压缩图片的路径
	 * @param maxLength
	 *            :长或者宽的最大值
	 * @param per
	 *            :图片质量
	 */
	public static void resize(String srcImgPath, String outImgPath,
			int maxLength) {
		resize(srcImgPath, outImgPath, maxLength, 1F);
	}

	/**
	 * 将图片按照指定的图片尺寸、图片质量压缩
	 *
	 * @param srcImgPath
	 *            :源图片路径
	 * @param outImgPath
	 *            :输出的压缩图片的路径
	 * @param new_w
	 *            :压缩后的图片宽
	 * @param new_h
	 *            :压缩后的图片高
	 * @param per
	 *            :百分比
	 */
	public static void resize(String srcImgPath, String outImgPath, int new_w,
			int new_h, float per) {
		// 得到图片
		BufferedImage src = inputImage(srcImgPath);
		int old_w = src.getWidth();
		// 得到源图宽
		int old_h = src.getHeight();
		// 得到源图长
		// 根据原图的大小生成空白画布
		BufferedImage tempImg = new BufferedImage(old_w, old_h,
				BufferedImage.TYPE_INT_RGB);
		// 在新的画布上生成原图的缩略图
		Graphics2D g = tempImg.createGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, old_w, old_h);
		g.drawImage(src, 0, 0, old_w, old_h, Color.white, null);
		g.dispose();
		BufferedImage newImg = new BufferedImage(new_w, new_h,
				BufferedImage.TYPE_INT_RGB);
		newImg.getGraphics().drawImage(
				tempImg.getScaledInstance(new_w, new_h, Image.SCALE_SMOOTH), 0,
				0, null);
		// 调用方法输出图片文件
		outImage(outImgPath, newImg, per);
	}

	/**
	 * 将图片按照指定的尺寸比例、图片质量压缩
	 *
	 * @param srcImgPath
	 *            :源图片路径
	 * @param outImgPath
	 *            :输出的压缩图片的路径
	 * @param ratio
	 *            :压缩后的图片尺寸比例
	 * @param per
	 *            :百分比
	 */
	public static void resize(String srcImgPath, String outImgPath,
			float ratio, float per) {
		// 得到图片
		BufferedImage src = inputImage(srcImgPath);
		int old_w = src.getWidth();
		// 得到源图宽
		int old_h = src.getHeight();
		// 得到源图长
		int new_w = 0;
		// 新图的宽
		int new_h = 0;
		// 新图的长
		BufferedImage tempImg = new BufferedImage(old_w, old_h,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = tempImg.createGraphics();
		g.setColor(Color.white);
		// 从原图上取颜色绘制新图g.fillRect(0, 0, old_w, old_h);
		g.drawImage(src, 0, 0, old_w, old_h, Color.white, null);
		g.dispose();
		// 根据图片尺寸压缩比得到新图的尺寸new_w = (int) Math.round(old_w * ratio);
		new_h = Math.round(old_h * ratio);
		BufferedImage newImg = new BufferedImage(new_w, new_h,
				BufferedImage.TYPE_INT_RGB);
		newImg.getGraphics().drawImage(
				tempImg.getScaledInstance(new_w, new_h, Image.SCALE_SMOOTH), 0,
				0, null);
		// 调用方法输出图片文件OutImage(outImgPath, newImg, per);
	}

	/**
	 * <b> 指定长或者宽的最大值来压缩图片 推荐使用此方法 </b>
	 *
	 * @param srcImgPath
	 *            :源图片路径
	 * @param outImgPath
	 *            :输出的压缩图片的路径
	 * @param maxLength
	 *            :长或者宽的最大值
	 * @param per
	 *            :图片质量
	 */
	public static void resize(String srcImgPath, String outImgPath,
			int maxLength, float per) {
		// 得到图片
		BufferedImage src = inputImage(srcImgPath);
		int old_w = src.getWidth();
		// 得到源图宽
		int old_h = src.getHeight();
		// 得到源图长
		int new_w = 0;
		// 新图的宽
		int new_h = 0;
		// 新图的长
		BufferedImage tempImg = new BufferedImage(old_w, old_h,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = tempImg.createGraphics();
		g.setColor(Color.white);
		// 从原图上取颜色绘制新图
		g.fillRect(0, 0, old_w, old_h);
		g.drawImage(src, 0, 0, old_w, old_h, Color.white, null);
		g.dispose();
		// 根据图片尺寸压缩比得到新图的尺寸
		if (old_w > old_h) {
			// 图片要缩放的比例
			new_w = maxLength;
			new_h = Math.round(old_h * ((float) maxLength / old_w));
		} else {
			new_w = Math.round(old_w * ((float) maxLength / old_h));
			new_h = maxLength;
		}
		BufferedImage newImg = new BufferedImage(new_w, new_h,
				BufferedImage.TYPE_INT_RGB);
		newImg.getGraphics().drawImage(
				tempImg.getScaledInstance(new_w, new_h, Image.SCALE_SMOOTH), 0,
				0, null);
		// 调用方法输出图片文件
		outImage(outImgPath, newImg, per);
	}

	/**
	 * 将图片压缩成指定宽度， 高度等比例缩放
	 *
	 * @param srcImgPath
	 * @param outImgPath
	 * @param width
	 * @param per
	 */
	public static void resizeMaxSize(String srcImgPath, String outImgPath,
			int width, float per) {
		// 得到图片
		BufferedImage src = inputImage(srcImgPath);
		int old_w = src.getWidth();
		// 得到源图宽
		int old_h = src.getHeight();
		// 得到源图长
		int new_w = 0;
		// 新图的宽
		int new_h = 0;
		// 新图的长
		BufferedImage tempImg = new BufferedImage(old_w, old_h,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = tempImg.createGraphics();
		g.setColor(Color.white);
		// 从原图上取颜色绘制新图
		g.fillRect(0, 0, old_w, old_h);
		g.drawImage(src, 0, 0, old_w, old_h, Color.white, null);
		g.dispose();
		// 根据图片尺寸压缩比得到新图的尺寸
		if (old_w > old_h) {
			// 图片要缩放的比例
			new_w = width;
			new_h = Math.round(old_h * ((float) width / old_w));
		} else {
			new_w = Math.round(old_w * ((float) width / old_h));
			new_h = width;
		}
		BufferedImage newImg = new BufferedImage(new_w, new_h,
				BufferedImage.TYPE_INT_RGB);
		newImg.getGraphics().drawImage(
				tempImg.getScaledInstance(new_w, new_h, Image.SCALE_SMOOTH), 0,
				0, null);
		// 调用方法输出图片文件
		outImage(outImgPath, newImg, per);
	}

	/**
	 * 将图片压缩质量
	 *
	 * @param srcImgPath
	 * @param outImgPath
	 * @param width
	 * @param per
	 */
	public static void changeQuality(String srcImgPath, String outImgPath,
			float per) {
		// 得到图片
		BufferedImage src = inputImage(srcImgPath);
		int old_w = src.getWidth();
		// 得到源图宽
		int old_h = src.getHeight();
		// 新图的长
		BufferedImage tempImg = new BufferedImage(old_w, old_h,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = tempImg.createGraphics();
		g.setColor(Color.white);
		// 从原图上取颜色绘制新图
		g.fillRect(0, 0, old_w, old_h);
		g.drawImage(src, 0, 0, old_w, old_h, Color.white, null);
		g.dispose();
		BufferedImage newImg = new BufferedImage(old_w, old_h,
				BufferedImage.TYPE_INT_RGB);
		newImg.getGraphics().drawImage(
				tempImg.getScaledInstance(old_w, old_h, Image.SCALE_SMOOTH), 0,
				0, null);
		// 调用方法输出图片文件
		outImage(outImgPath, newImg, per);
	}

	/**
	 * 将图片压缩成指定宽度， 高度等比例缩放
	 *
	 * @param srcImgPath
	 * @param outImgPath
	 * @param width
	 * @param per
	 */
	public static void resizeFixedWidth(String srcImgPath, String outImgPath,
			int width, float per) {
		// 得到图片
		BufferedImage src = inputImage(srcImgPath);
		int old_w = src.getWidth();
		// 得到源图宽
		int old_h = src.getHeight();
		// 得到源图长
		int new_w = 0;
		// 新图的宽
		int new_h = 0;
		// 新图的长
		BufferedImage tempImg = new BufferedImage(old_w, old_h,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = tempImg.createGraphics();
		g.setColor(Color.white);
		// 从原图上取颜色绘制新图
		g.fillRect(0, 0, old_w, old_h);
		g.drawImage(src, 0, 0, old_w, old_h, Color.white, null);
		g.dispose();
		// 根据图片尺寸压缩比得到新图的尺寸
		// 图片要缩放的比例
		new_w = width;
		new_h = Math.round(old_h * ((float) width / old_w));
		BufferedImage newImg = new BufferedImage(new_w, new_h,
				BufferedImage.TYPE_INT_RGB);
		newImg.getGraphics().drawImage(
				tempImg.getScaledInstance(new_w, new_h, Image.SCALE_SMOOTH), 0,
				0, null);
		// 调用方法输出图片文件
		outImage(outImgPath, newImg, per);
	}

	/**
	 * 将图片压缩成指定宽度， 高度等比例缩放
	 *
	 * @param srcImgPath
	 * @param outImgPath
	 * @param height
	 * @param per
	 */
	public static void resizeFixedHeight(String srcImgPath, String outImgPath,
			int height, float per) {
		// 得到图片
		BufferedImage src = inputImage(srcImgPath);
		int old_w = src.getWidth();
		// 得到源图宽
		int old_h = src.getHeight();
		// 得到源图长
		int new_w = 0;
		// 新图的宽
		int new_h = 0;
		// 新图的长
		BufferedImage tempImg = new BufferedImage(old_w, old_h,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = tempImg.createGraphics();
		g.setColor(Color.white);
		// 从原图上取颜色绘制新图
		g.fillRect(0, 0, old_w, old_h);
		g.drawImage(src, 0, 0, old_w, old_h, Color.white, null);
		g.dispose();
		// 根据图片尺寸压缩比得到新图的尺寸
		// 图片要缩放的比例
		new_w = Math.round(old_w * ((float) height / old_h));
		new_h = height;
		BufferedImage newImg = new BufferedImage(new_w, new_h,
				BufferedImage.TYPE_INT_RGB);
		newImg.getGraphics().drawImage(
				tempImg.getScaledInstance(new_w, new_h, Image.SCALE_SMOOTH), 0,
				0, null);
		// 调用方法输出图片文件
		outImage(outImgPath, newImg, per);
	}

	/**
	 * 将图片文件输出到指定的路径，并可设定压缩质量
	 *
	 * @param outImgPath
	 * @param newImg
	 * @param per
	 */
	public static void outImage(String outImgPath, BufferedImage newImg,
			float per) {
		// 判断输出的文件夹路径是否存在，不存在则创建
		File file = new File(outImgPath);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		// 输出到文件流
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(outImgPath);
			// 得到指定Format图片的writer
			Iterator<ImageWriter> iter = ImageIO
					.getImageWritersByFormatName("jpeg");// 得到迭代器
			ImageWriter writer = iter.next(); // 得到writer
			// 得到指定writer的输出参数设置(ImageWriteParam )
			ImageWriteParam iwp = writer.getDefaultWriteParam();
			iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT); // 设置可否压缩
			iwp.setCompressionQuality(per); // 设置压缩质量参数
			iwp.setProgressiveMode(ImageWriteParam.MODE_DISABLED);
			ColorModel colorModel = ColorModel.getRGBdefault();
			// 指定压缩时使用的色彩模式
			iwp.setDestinationType(new javax.imageio.ImageTypeSpecifier(
					colorModel, colorModel.createCompatibleSampleModel(16, 16)));
			writer.setOutput(ImageIO.createImageOutputStream(fos));
			IIOImage iIamge = new IIOImage(newImg, null, null);
			writer.write(null, iIamge, iwp);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	/**
	 * 图片剪切工具方法
	 *
	 * @param srcfile
	 *            源图片
	 * @param outfile
	 *            剪切之后的图片
	 * @param x
	 *            剪切顶点 X 坐标
	 * @param y
	 *            剪切顶点 Y 坐标
	 * @param width
	 *            剪切区域宽度
	 * @param height
	 *            剪切区域高度
	 *
	 * @throws IOException
	 */
	public static void cut(File srcfile, File outfile, int x, int y, int width,
			int height) throws IOException {
		FileInputStream is = null;
		ImageInputStream iis = null;
		try {
			// 读取图片文件
			is = new FileInputStream(srcfile);

			/*
			 * 返回包含所有当前已注册 ImageReader 的 Iterator，这些 ImageReader 声称能够解码指定格式。
			 * 参数：formatName - 包含非正式格式名称 .（例如 "jpeg" 或 "tiff"）等 。
			 */
			Iterator<ImageReader> it = ImageIO
					.getImageReadersByFormatName("jpg");
			ImageReader reader = it.next();
			// 获取图片流
			iis = ImageIO.createImageInputStream(is);

			/*
			 * <p>iis:读取源.true:只向前搜索 </p>.将它标记为 '只向前搜索'。
			 * 此设置意味着包含在输入源中的图像将只按顺序读取，可能允许 reader 避免缓存包含与以前已经读取的图像关联的数据的那些输入部分。
			 */
			reader.setInput(iis, true);

			/*
			 * <p>描述如何对流进行解码的类<p>.用于指定如何在输入时从 Java Image I/O
			 * 框架的上下文中的流转换一幅图像或一组图像。用于特定图像格式的插件 将从其 ImageReader 实现的
			 * getDefaultReadParam 方法中返回 ImageReadParam 的实例。
			 */
			ImageReadParam param = reader.getDefaultReadParam();

			/*
			 * 图片裁剪区域。Rectangle 指定了坐标空间中的一个区域，通过 Rectangle 对象
			 * 的左上顶点的坐标（x，y）、宽度和高度可以定义这个区域。
			 */
			Rectangle rect = new Rectangle(x, y, width, height);

			// 提供一个 BufferedImage，将其用作解码像素数据的目标。
			param.setSourceRegion(rect);

			/*
			 * 使用所提供的 ImageReadParam 读取通过索引 imageIndex 指定的对象，并将 它作为一个完整的
			 * BufferedImage 返回。
			 */
			BufferedImage bi = reader.read(0, param);

			// 保存新图片
			// ImageIO.write(bi, "jpg", outfile);
			outImage(outfile.getAbsolutePath(), bi, 1);
		} finally {
			if (is != null) {
				is.close();
			}
			if (iis != null) {
				iis.close();
			}
		}
	}

	public static void main(String args[]) throws Exception {
		String tarDir = "d:/test/";
		String dstImg = "notok2";
		String srcImg = "d:/notpng.png";

		srcImg = "d:/notpng.png";
		dstImg = "notpng";
		System.out.println(srcImg + " is " + getFormatName(new File(srcImg)));
		testMethod(tarDir, dstImg, srcImg);

		srcImg = "d:/isjpg.jpg";
		dstImg = "isjpg";
		System.out.println(srcImg + " is " + getFormatName(new File(srcImg)));
		testMethod(tarDir, dstImg, srcImg);

		srcImg = "d:/isnotrgbjpg.jpg";
		dstImg = "isnotrgbjpg";
		System.out.println(srcImg + " is " + getFormatName(new File(srcImg)));
		testMethod(tarDir, dstImg, srcImg);

		srcImg = "d:/ispng.png";
		dstImg = "ispng";
		System.out.println(srcImg + " is " + getFormatName(new File(srcImg)));
		testMethod(tarDir, dstImg, srcImg);

		srcImg = "d:/isbmp.bmp";
		dstImg = "isbmp";
		System.out.println(srcImg + " is " + getFormatName(new File(srcImg)));
		testMethod(tarDir, dstImg, srcImg);

		srcImg = "d:/isjpg2.jpg";
		dstImg = "isjpg2";
		System.out.println(srcImg + " is " + getFormatName(new File(srcImg)));
		testMethod(tarDir, dstImg, srcImg);

		srcImg = "d:/isgif.gif";
		dstImg = "isgif";
		System.out.println(srcImg + " is " + getFormatName(new File(srcImg)));
		testMethod(tarDir, dstImg, srcImg);

		srcImg = "d:/notok1.jpg";
		dstImg = "notok1";
		System.out.println(srcImg + " is " + getFormatName(new File(srcImg)));
		testMethod(tarDir, dstImg, srcImg);

		srcImg = "d:/notok2.jpg";
		dstImg = "notok2";
		System.out.println(srcImg + " is " + getFormatName(new File(srcImg)));
		testMethod(tarDir, dstImg, srcImg);

		srcImg = "d:/notok3.jpg";
		dstImg = "notok3";
		System.out.println(srcImg + " is " + getFormatName(new File(srcImg)));
		testMethod(tarDir, dstImg, srcImg);

		srcImg = "d:/istif.tif";
		dstImg = "istif";
		System.out.println(srcImg + " is " + getFormatName(new File(srcImg)));
		testMethod(tarDir, dstImg, srcImg);
	}

	private static void testMethod(String tarDir, String dstImg, String srcImg) {
		long currentTimeMillis = System.currentTimeMillis();
		resize(srcImg, tarDir + dstImg + "-200px.jpg", 200);
		resize(srcImg, tarDir + dstImg + "-400x500.jpg", 400, 500);
		resize(srcImg, tarDir + dstImg + "-220x220.jpg", 220, 220);
		resize(srcImg, tarDir + dstImg + "-220px-7.jpg", 220, 0.7F);
		resize(srcImg, tarDir + dstImg + "-400x500-8.jpg", 400, 500, 0.8F);
		resizeFixedWidth(srcImg, tarDir + dstImg + "-width_800-8.jpg", 800,
				0.8F);
		resizeFixedHeight(srcImg, tarDir + dstImg + "-height_800-5.jpg", 800,
				0.5F);
		resizeFixedHeight(srcImg, tarDir + dstImg + "-height_800-6.jpg", 800,
				0.6F);
		resizeFixedHeight(srcImg, tarDir + dstImg + "-height_800-7.jpg", 800,
				0.7F);
		resizeFixedHeight(srcImg, tarDir + dstImg + "-height_800-8.jpg", 800,
				0.8F);
		resizeFixedHeight(srcImg, tarDir + dstImg + "-height_800-9.jpg", 800,
				0.9F);
		System.out.println("time:"
				+ (System.currentTimeMillis() - currentTimeMillis));
	}
}