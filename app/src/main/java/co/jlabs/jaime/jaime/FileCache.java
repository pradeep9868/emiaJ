package co.jlabs.jaime.jaime;

import android.content.Context;

import java.io.File;

public class FileCache {

	private File cacheDir;

	public FileCache(Context context) {
		// Find the dir to save cached images
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			cacheDir = new File(
					android.os.Environment.getExternalStorageDirectory(),
					"PraJaimeCache");
		else
			cacheDir = context.getCacheDir();
		if (!cacheDir.exists())
			cacheDir.mkdirs();
	}

	public File getFile(String url,int i) {
		String filename = String.valueOf(url.hashCode());
		// String filename = URLEncoder.encode(url);
		File f = new File(cacheDir, filename+"_"+i);
		return f;

	}
    public File getFile_noencode(String url) {
        File f = new File(cacheDir, url);
        return f;

    }
    public String getFileName(String url) {
        return String.valueOf(url.hashCode());
    }

	public void clear() {
		File[] files = cacheDir.listFiles();
		if (files == null)
			return;
		for (File f : files)
			f.delete();
	}

}