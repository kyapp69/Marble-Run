package nttu.edu.score;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;

public class Format implements Serializable {
	
	private static final long serialVersionUID = 1L;
	public List<Format> sessions = new ArrayList<Format>();
	
	public Map<Integer, Long> highScores;
	public Map<String, Integer> profiles;
	public Map<AbstractMap.SimpleEntry<String, Integer>, Long> entries;
	
	public Format() {
		try {
			Log.d("Format()", "Creating new file, 'format.dat'. ");
			File directory = new File("/data/data/nttu.edu/files");
			if (directory.exists()) {
				FileOutputStream out = new FileOutputStream(new File(directory, "format.dat"));
				out.close();
			}
		}
		catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addEntry(String player, long points) {
		Format format = new Format();
		sessions.add(format);
		format = null;
	}
	
	public void addBestEntry(int stageNumber, long score) {
		// If true, the values has been changed. If false, we do nothing.
		if (highScores == null)
			highScores = new LinkedHashMap<Integer, Long>();
		Integer compare = Integer.valueOf(stageNumber);
		Long t = Long.valueOf(score);
		if (highScores.containsKey(compare)) {
			Long s = Long.valueOf(highScores.get(compare));
			if (s < t) {
				highScores.put(compare, t);
			}
		}
		else
			highScores.put(compare, t);
	}
	
	public void addBestEntryAndName(int stageNumber, long score, String name) {
		if (entries == null)
			entries = new LinkedHashMap<AbstractMap.SimpleEntry<String, Integer>, Long>();
		Integer stage = Integer.valueOf(stageNumber);
		Long points = Long.valueOf(score);
		AbstractMap.SimpleEntry<String, Integer> entryKey = new AbstractMap.SimpleEntry<String, Integer>(name, stage);
		if (entries.containsKey(entryKey)) {
			Long oldPoints = Long.valueOf(entries.get(entryKey));
			if (points > oldPoints) {
				entries.put(entryKey, points);
			}
		}
		else {
			entries.put(entryKey, points);
		}
	}
	
	public Long getScore(int key) {
		if (highScores != null) {
			Integer k = Integer.valueOf(key);
			Long temp = highScores.get(k);
			if (temp == null)
				return 0L;
			return temp;
		}
		return 0L;
	}
	
	public static Format loadFormat(Context context) {
		FileInputStream in = null;
		ObjectInputStream inn = null;
		Format format = null;
		try {
			in = context.openFileInput("format.dat");
			inn = new ObjectInputStream(new BufferedInputStream(in));
			if (inn != null){
				format = (Format) inn.readObject();
			}
		}
		catch (FileNotFoundException e) {
			Log.e("PlayActivity", "FileNotFoundException", e);
			format = new Format();
		}
		catch (OptionalDataException e) {
			Log.e("PlayActivity", "OptionalDataException", e);
			format = new Format();
		}
		catch (ClassNotFoundException e) {
			Log.e("PlayActivity", "ClassNotFoundException", e);
			format = new Format();
		}
		catch (IOException e) {
			Log.e("PlayActivity", "IOException", e);
			format = new Format();
		}
		finally {
			if (in != null) {
				try {
					in.close();
				}
				catch (IOException e) {
					Log.e("PlayActivity", "IOException", e);
				}
			}
			if (inn != null) {
				try {
					inn.close();
				}
				catch (IOException e) {
					Log.e("PlayActivity", "IOException", e);
				}
			}
			if (format ==  null){
				format = new Format();
			}
		}
		return format;
	}
}
