/*
 * <author>Hankcs</author>
 * <email>me@hankcs.com</email>
 * <create-date>2017-11-02 12:09</create-date>
 *
 * <copyright file="Demo.java" company="码农场">
 * Copyright (c) 2017, 码农场. All Right Reserved, http://www.hankcs.com/
 * This source is subject to Hankcs. Please contact Hankcs to get more information.
 * </copyright>
 */
package com.jrj.test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.hankcs.hanlp.corpus.MSR;
import com.hankcs.hanlp.corpus.io.IOUtil;
import com.hankcs.hanlp.mining.word2vec.DocVectorModel;
import com.hankcs.hanlp.mining.word2vec.Word2VecTrainer;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;

/**
 * 演示词向量的训练与应用
 *
 * @author hankcs
 */
public class DemoWord2Vec {
	private static final String TRAIN_FILE_NAME = MSR.TRAIN_PATH;
	private static final String MODEL_FILE_NAME = "/data/baidu1/news_msr.txt";

	public static void main(String[] args) throws IOException {
		WordVectorModel wordVectorModel = trainOrLoadModel();

		// 文档向量
		DocVectorModel docVectorModel = new DocVectorModel(wordVectorModel);
		@SuppressWarnings("deprecation")
		List<String> ds = FileUtils.readLines(new File("/data/baidu1/news_msr.txt"), "UTF-8");
		String paper = "中新经纬客户端9月20日电 工信部20日发布2019年1-8月通信业经济运行情况，数据显示，1-8月，电信业务收入累计完成8881亿元，同比下降0.02%，降幅较1-7月缩小0.13个百分点。另外，移动互联网流量增速回落至两位数。1-8月，移动互联网累计流量达777亿GB，同比增速降至96.5%。\n"
				+ "\n" + "　　工信部介绍，电信业务收入与去年同期规模相当。1-8月，电信业务收入累计完成8881亿元，同比下降0.02%，降幅较1-7月缩小0.13个百分点。\n" + "\n"
				+ "　　另外，前8月三家基础电信企业实现固定通信业务收入2800亿元，同比增长9.5%，在电信业务收入中占31.5%；实现移动通信业务收入6081亿元，同比下降3.9%，降幅较1-7月收窄0.2个百分点，占电信业务收入的68.5%。\n"
				+ "\n"
				+ "　　1-8月，三家基础电信企业完成移动数据及互联网业务收入4078亿元，同比增长0.4%，扭转1-7月同比下降的趋势，在电信业务收入中占45.9%，拉动电信业务收入增长0.19个百分点。\n"
				+ "\n"
				+ "　　数据还显示，4G用户在移动电话用户中的占比稳步提高。截至8月底，三家基础电信企业的移动电话用户总数达15.96亿户，同比增长3.8%，较7月末增加491万户。其中， 4G用户规模为12.57亿户，占移动电话用户的78.8%，占比较上年末提高4.4个百分点。\n"
				+ "\n"
				+ "　　电信业务使用情况上，移动互联网流量增速回落至两位数。1-8月，移动互联网累计流量达777亿GB，同比增速降至96.5%；其中通过手机上网的流量达到774亿GB，占移动互联网总流量的99.6%，同比增速降至98.9%。8月当月户均移动互联网接入流量(DOU)达到8.64GB，同比增速换挡，由上年末132.5%降至78.3%。\n"
				+ "\n" + "回落至两位数，今年前8月移动互联网流量增速逾96%\n" + "\n" + "　　来源：工信微报\n" + "\n"
				+ "　　移动短信业务量和收入均保持较快增长。在服务登录和身份认证等服务普及带动下，短信业务的业务量和收入保持同步增长。1-8月，全国移动短信业务量同比增长40.2%，移动短信业务收入完成269亿元，同比增长5.4%。\n"
				+ "\n"
				+ "　　分地区看，东北部百兆以上固定宽带接入用户渗透率保持领先。截至8月底，东、中、西和东北部地区100Mbps及以上固定宽带接入用户分别达到15842万户、8347万户、8749万户和2250万户，占本地区固定互联网宽带接入用户总数的比重分别为80%、79.3%、77.3%、83.8%，东北部地区占比小幅领先。(中新经纬APP)";
		for (String d : ds) {
			System.out.println(docVectorModel.similarity(d, paper));
		}
		// System.out.println(docVectorModel.similarity(documents[0], documents[4]));

	}

//    static void printNearest(String word, WordVectorModel model)
//    {
//        System.out.printf("\n                                                Word     Cosine\n------------------------------------------------------------------------\n");
//        for (Map.Entry<String, Float> entry : model.nearest(word))
//        {
//            System.out.printf("%50s\t\t%f\n", entry.getKey(), entry.getValue());
//        }
//    }
//
//    static void printNearestDocument(String document, String[] documents, DocVectorModel model)
//    {
//        printHeader(document);
//        for (Map.Entry<Integer, Float> entry : model.nearest(document))
//        {
//            System.out.printf("%50s\t\t%f\n", documents[entry.getKey()], entry.getValue());
//        }
//    }

	private static void printHeader(String query) {
		System.out.printf(
				"\n%50s          Cosine\n------------------------------------------------------------------------\n",
				query);
	}

	static WordVectorModel trainOrLoadModel() throws IOException {
		if (!IOUtil.isFileExisted(MODEL_FILE_NAME)) {
			if (!IOUtil.isFileExisted(TRAIN_FILE_NAME)) {
				System.err.println("语料不存在，请阅读文档了解语料获取与格式：https://github.com/hankcs/HanLP/wiki/word2vec");
				System.exit(1);
			}
			Word2VecTrainer trainerBuilder = new Word2VecTrainer();
			return trainerBuilder.train(TRAIN_FILE_NAME, MODEL_FILE_NAME);
		}

		return loadModel();
	}

	static WordVectorModel loadModel() throws IOException {
		return new WordVectorModel(MODEL_FILE_NAME);
	}
}
