package com.jrj.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;

public class TrainPrepared {

	public static void main(String[] args) {
		String f="/data/baidu1/news2016zh_train.json";
		FileInputStream fi=null;
		BufferedReader fp=null;
		try {
			fi=new FileInputStream(f);
			InputStreamReader fileReader = new InputStreamReader(fi,"utf-8");
			 fp = new BufferedReader(fileReader);
			String line;
			int i=0;
			do {
				line=fp.readLine();
				String l=JSON.parseObject(line).getString("content");
	            List<Term> termList = HanLP.segment(l);
	            for(Term t:termList) {
	            	  System.out.print(t.word+" ");
	            }
	            System.out.println();
				i++;
				if(i>100)break;
			}while(line!=null);
			
		} catch (FileNotFoundException e) {
		
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				fp.close();
				fi.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}

	}

}
