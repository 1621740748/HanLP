package com.jrj.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.stopword.CoreStopWordDictionary;
import com.hankcs.hanlp.seg.common.Term;

public class TrainPrepared {

	public static void main(String[] args) {
		String f = "/data/baidu1/news2016zh_train.json";
		FileInputStream fi = null;
		BufferedReader fp = null;
		String fo="/data/baidu1/news2016zh_train.txt";
		BufferedWriter fw=null;
		try {
			fw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fo),"utf-8"));
			fi = new FileInputStream(f);
			InputStreamReader fileReader = new InputStreamReader(fi, "utf-8");
			fp = new BufferedReader(fileReader);
			String line=null;
			int i = 0;
			StringBuilder ww=new StringBuilder();
			do {
				try {
					line = fp.readLine();
					String l=null;
					if(line==null) {
						break;
					}
					try {
						l = JSON.parseObject(line).getString("content");
					} catch (Exception e) {
						
						e.printStackTrace();
					}
					if(l==null||l.equals("")) {
						continue;
					}
					List<Term> termList = HanLP.segment(l);
					for(Term t:termList) {
						if(CoreStopWordDictionary.contains(t.word)) {
							continue;
						}
						System.out.print(t.word+" ");
						ww.append(t.word).append(" ");
					}
					//				JiebaSegmenter segmenter = new JiebaSegmenter();
					//				List<String> aa = segmenter.sentenceProcess(l);
					//				for (String s : aa) {
					//					if (CoreStopWordDictionary.contains(s)) {
					//						continue;
					//					}
					//					System.out.print(s+" ");
					//				}
					System.out.println();
					ww.append("\n");
					i++;
					if (i>0&&i%10000==0) {
						String s=ww.toString();
						fw.write(s);
						ww.setLength(0);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} while (line != null);
			//写入剩余的数据
			if(ww.length()>0) {
				String s=ww.toString();
				fw.write(s);
			}

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fp.close();
				fi.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

}
