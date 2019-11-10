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
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.hankcs.hanlp.HanLP;
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
		Long start=System.currentTimeMillis();
		WordVectorModel wordVectorModel = trainOrLoadModel();
		Long end=System.currentTimeMillis();
		System.out.println("load model takes:"+(end-start));


		// 文档向量
		DocVectorModel docVectorModel = new DocVectorModel(wordVectorModel);
		@SuppressWarnings("deprecation")
		List<String> ds = FileUtils.readLines(new File("/data/baidu1/industry"), "UTF-8");
		String paper = "全面取消外资银行等金融机构业务范围限制\n" + 
				"0评论 2019-11-08 02:58:06 来源：证券时报 作者：孙璐璐 如何根据对手盘判断龙头股买点\n" + 
				"\n" + 
				"《意见》提出包括深化对外开放、加大投资促进力度、深化投资便利化改革、保护外商投资合法权益等四方面共计20条政策措施。加快金融业开放进程，全面取消在华外资银行、证券公司、基金管理公司等金融机构业务范围限制，丰富市场供给，增强市场活力。\n" + 
				"\n" + 
				"　　国务院近日印发《关于进一步做好利用外资工作的意见》。《意见》提出包括深化对外开放、加大投资促进力度、深化投资便利化改革、保护外商投资合法权益等四方面共计20条政策措施。\n" + 
				"\n" + 
				"　　在深化对外开放方面，《意见》提出，将支持外商投资新开放领域，继续压减全国和自由贸易试验区外商投资准入负面清单，全面清理取消未纳入负面清单的限制措施。加快金融业开放进程，全面取消在华外资银行、证券公司、基金管理公司等金融机构业务范围限制，丰富市场供给，增强市场活力。减少外国投资者投资设立银行业、保险业机构和开展相关业务的数量型准入条件，取消外国银行来华设立外资法人银行、分行的总资产要求，取消外国保险经纪公司在华经营保险经纪业务的经营年限、总资产要求。扩大投资入股外资银行和外资保险机构的股东范围，取消中外合资银行中方唯一或主要股东必须是金融机构的要求，允许外国保险集团公司投资设立保险类机构。继续支持按照内外资一致的原则办理外资保险公司及其分支机构设立及变更等行政许可事项。2020年取消证券公司、证券投资基金管理公司、期货公司、寿险公司外资持股比例不超过51%的限制。\n" + 
				"\n" + 
				"　　在加大投资促进力度方面，《意见》指出，要优化外商投资企业科技创新服务，提升自由贸易试验区建设水平，提升开放平台引资质量，支持地方加大对外资的招商引资力度。\n" + 
				"\n" + 
				"　　在促进投资便利化方面，《意见》指出，尽快出台具体措施，支持外商投资企业扩大人民币跨境使用。扩大资本项目收入支付便利化改革试点范围。推进企业发行外债登记制度改革，完善全口径跨境融资宏观审慎管理政策，支持外商投资企业自主选择借用外债模式，降低融资成本。鼓励外商投资企业资本金依法用于境内股权投资。\n" + 
				"\n" + 
				"　　此外，《意见》指出，保护外商投资合法权益。全面贯彻外商投资法，建立健全外商投资企业投诉受理机构，强化监管政策执行规范性，提高行政规范性文件制定透明度，发挥知识产权司法保护重要作用，完善知识产权保护工作机制，鼓励外商投资企业参与我国医疗器械、食品药品等标准制定，保障供应商依法平等参与政府采购。";
		
		
		
		class NameValue{
		   public String name;
		   public float value;
		public NameValue(String name, float value) {
			super();
			this.name = name;
			this.value = value;
		}
		   
	   }
	   List<NameValue> result=new LinkedList<>();
		Long startTime=System.currentTimeMillis();
		for (String d : ds) {
			float f=docVectorModel.similarity(d, paper);
			//System.out.println();
			result.add(new NameValue(d,f));
		}
		result.sort((n1,n2)->{ 
			if(n1.value<=n2.value)return 1;
			else if(n1.value==n2.value)return 0;
			return -1;
			}
		);
		for(NameValue v:result) {
			System.out.println(v.name+":"+v.value);
		}
		Long endTime=System.currentTimeMillis();
		System.out.println("take time:"+(endTime-startTime));
		
		List<String> keywordList = HanLP.extractKeyword(paper, 10);
		System.out.println(keywordList);

	}


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
