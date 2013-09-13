package br.com.radiofederal.app.util;

import java.io.IOException;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;

public class MediaPlayerFacade {

	private static MediaPlayer mp = new MediaPlayer();
		
	
	
	public static void prepareAsync(String dataSource) {
		
		if (mp == null) {
			mp = new MediaPlayer();
			mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
		}
		
		try {
			mp.setDataSource(dataSource);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mp.prepareAsync();		
		

	}
	
	public static void resume() {
		if (mp == null) {
			return;
		}
		mp.start();
	}
	
	public static void play() {
		if (mp == null) {
			return;
		}
		
		mp.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				mp.start();
			}
		});
		
	}
	
	public static void stop() {
		if (mp == null) {
			return;
		}
		
		mp.stop();			
	}
	
	public static void pause() {
		
		if (mp == null) {
			return;
		}
		mp.pause();
	}
	
	
	public static void dispose() {
		if (mp == null) {
			return;
		}
		try {
			if (mp.isPlaying()) {
				mp.stop();
			}
			mp.release();
		} catch (Exception e) {
			
		}
		
		mp = null;
	}
}
