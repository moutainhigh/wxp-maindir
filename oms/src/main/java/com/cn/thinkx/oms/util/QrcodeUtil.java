package com.cn.thinkx.oms.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cn.thinkx.pms.base.utils.ReadPropertiesFile;
import com.swetake.util.Qrcode;

public class QrcodeUtil {
	
	private static Logger LOG = LoggerFactory.getLogger(QrcodeUtil.class);
	
	/**
	 * 生成二维码(QRCode)图片
	 * 
	 * @param content
	 *            二维码图片的内容
	 * @param imgPath
	 *            生成二维码图片完整的路径
	 * @param ccbpath
	 *            二维码图片中间的logo路径
	 */
	public static int createQRCode(String content, String imgPath, String ccbPath, int size) {
		try {
			Qrcode qrcodeHandler = new Qrcode();
			// 设置二维码排错率，可选L(7%)、M(15%)、Q(25%)、H(30%)，排错率越高可存储的信息越少，但对二维码清晰度的要求越小
			qrcodeHandler.setQrcodeErrorCorrect('H');
			// N代表数字,A代表字符a-Z,B代表其他字符
			qrcodeHandler.setQrcodeEncodeMode('B');
			// 设置设置二维码版本，取值范围1-40，值越大尺寸越大，可存储的信息越大
			qrcodeHandler.setQrcodeVersion(size);
			// 图片尺寸  
            int imgSize = 67 + 12 * (size - 1); 

			byte[] contentBytes = content.getBytes("utf-8");
			BufferedImage bufImg = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_RGB);
			Graphics2D gs = bufImg.createGraphics();

			gs.setBackground(Color.WHITE);
			gs.clearRect(0, 0, imgSize, imgSize);

			// 设定图像颜色 > BLACK
			gs.setColor(Color.black);
			// 设置偏移量 不设置可能导致解析出错
			int pixoff = 2;
			// 输出内容 > 二维码
			if (contentBytes.length > 0 && contentBytes.length < 150) {
				boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);
				for (int i = 0; i < codeOut.length; i++) {
					for (int j = 0; j < codeOut.length; j++) {
						if (codeOut[j][i]) {
							gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);
						}
					}
				}
			} else {
				LOG.info("QRCode content bytes length = [{}] not in [ 0,125]. ", contentBytes.length);
				return -1;
			}
			
			Image img = ImageIO.read(new File(ccbPath));// 实例化一个Image对象。
			gs.drawImage(img, imgSize / 3, imgSize / 3 + 5, null);
			gs.dispose();
			bufImg.flush();

			// 生成二维码QRCode图片
			File imgFile = new File(imgPath);
			ImageIO.write(bufImg, "png", imgFile);
		} catch (Exception e) {
			LOG.error("QRCode created error: ", e);
			return -100;
		}
		
		return 0;
	}
	
	/**
	 * 调整图片大小 
	 * 
	 * @param srcImgPath
	 *            原图片路径
	 * @param distImgPath
	 *            转换大小后图片路径
	 * @param width
	 *            转换后图片宽度
	 * @param height
	 *            转换后图片高度
	 */
	public static void resizeImage(String srcImgPath, String distImgPath, int width, int height) throws IOException {
		File srcFile = new File(srcImgPath);
		Image srcImg = ImageIO.read(srcFile);
		BufferedImage buffImg = null;
		buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		buffImg.getGraphics().drawImage(srcImg.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
		
		ImageIO.write(buffImg, "JPEG", new File(distImgPath));
	}
	
	/*
     * 获取项目中LOGO图片的完整目录
     * 因为tomcat和weblogic获取的根目录不一致，所以需要此方法
     */
	public static String getLogoUrl(HttpServletRequest request, String subPath) {
		String fileDirPath = request.getSession().getServletContext().getRealPath(subPath);
		if (fileDirPath == null) {
			// 如果返回为空，则表示服务器为weblogic，则需要使用另外的方法
			try {
				fileDirPath = request.getSession().getServletContext().getResource(subPath).getFile();
			} catch (MalformedURLException e) {
				LOG.error("获取LOGO图片[" + fileDirPath + "]出错", e);
			}
		} 
		return fileDirPath;
	}
	
	public static void main(String[] args) {
		String url = ReadPropertiesFile.getInstance().getProperty("SHOP_QRCODE_URL", null);
		int i = QrcodeUtil.createQRCode("http://dfk.tao-lue.com/om.html?p=GN5JGugOpJ1PQdoiSZqxDwMKRhYt57mgZtMzvpa6vyw%3D", url + "/12345.png", url + "/logo.png", 10);
		System.out.println(i);
		/*try {
			resizeImage(url + "/logo.png", url + "/logo1.png", 65, 50);
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
}
