package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class ObjReader {

	String filePath;
	ArrayList<Float> vList, nList;

	public ObjReader(String filePath) {
		filePath += ".obj";
		this.filePath = filePath;
		vList = new ArrayList<Float>();
		nList = new ArrayList<Float>();
		try {
			getData();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public float[] getVectorList() {
		float[] send = new float[vList.size()];
		Iterator<Float> iter = vList.iterator();
		int i = 0;
		while (iter.hasNext()) {
			send[i] = iter.next();
			i++;
		}
		return send;
	}
	
	public float[] getNormalList() {
		float[] send = new float[nList.size()];
		Iterator<Float> iter = nList.iterator();
		int i = 0;
		while (iter.hasNext()) {
			send[i] = iter.next();
			i++;
		}
		return send;
	}

	private void getData() throws IOException {
		String str;
		String[] strA;
		float v;

		BufferedReader reader = new BufferedReader(new FileReader(filePath));

		while ((str = reader.readLine()) != null) {

			strA = str.split(" ");

			if (strA[0].equalsIgnoreCase("v")) {
				for (int i = 1; i < 4; i++) {
					v = Float.parseFloat(strA[i]);
					vList.add(v);
				}
			}

			if (strA[0].equalsIgnoreCase("vn")) {
				for (int i = 1; i < 4; i++) {
					v = Float.parseFloat(strA[i]);
					nList.add(v);
				}
			}

			if (strA[0].equalsIgnoreCase("f")) {
				for (int i = 1; i < 4; i++) {

				}
			}
		}
		reader.close();
	}
}
