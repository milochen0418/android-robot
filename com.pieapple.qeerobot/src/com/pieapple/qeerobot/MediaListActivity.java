package com.pieapple.qeerobot;

import java.io.IOException;

//import com.joshclemm.android.tabs.R;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MediaListActivity extends Activity implements OnClickListener {

	
	private static long[] sAllSongsId;
	private static int sAllSongsIdx;
	private static int sSongCount = -1;	
	private static int sRandomCacheEnd;
	private static final int ALL_SONG_POPULATE_SIZE =10000;
	private static final Song[] sAllSongsCache = new Song[ALL_SONG_POPULATE_SIZE];

	public MediaPlayer mMediaPlayer;
	
	public ListView mListView;
	public ArrayAdapter mAdapter;
	public String[] songsArray;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_media_list);
		findViewById(R.id.musicplayer_bottom_btn).setOnClickListener(this);
		mListView = (ListView)findViewById(R.id.musicplayer_list_view);
		
		this.showAllSongs();
		int count = 0;
		
		while(sAllSongsCache != null &&  count < sAllSongsCache.length && sAllSongsCache[count] != null ) count++;
		
		//songsArray = new String[sAllSongsCache.length];
		songsArray = new String[count];
		for (int i = 0; i < songsArray.length && sAllSongsCache[i]!=null; ++i ) {
			
			//songsArray[i] = "Song " + i;
			String title = "Unknown";
			if(sAllSongsCache[i].title != null) {
				title = sAllSongsCache[i].title; 
			}
			songsArray[i] = ""+i+". " + title;
			
		}
		
		mAdapter = new ArrayAdapter (this, android.R.layout.simple_list_item_1,songsArray);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(
			new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					Log.i("DEBUG_TAG","position = " + position + ", id = " + id);
					MediaListActivity.this.playSongWithIdx((int)id);
					
				}				
			}		
		);
	}
		
	
	
	public void showAllSongs () {
		sAllSongsId = queryAllSongs(this.getContentResolver());
		makeAllSongsList(this.getContentResolver());
		int idx; 
		for (idx = 0; idx < sAllSongsCache.length; idx++) {
			Song song = sAllSongsCache[idx];
			if(song !=null) {
				if(song.title != null) {
					Log.i("DEBUG_TAG", "songs["+idx+"].title = "+song.title);		
					Log.i("DEBUG_TAG", "songs["+idx+"].path = "+song.path);
				}
			}
		}
	}
	
	public static void makeAllSongsList( ContentResolver resolver) {
		long[] songs = sAllSongsId;

		Uri media = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		Log.i("DEBUG_TAG","MediaStore.Audio.Media.EXTERNAL_CONTENT_URI = " + MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.toString());
		
		if(sAllSongsId == null || sAllSongsId.length == 0) return;
		
		
		StringBuilder selection = new StringBuilder("_ID IN (");
		
		
		for (int i = 0; i < songs.length; ++i) {
			if (i > 0) {
				selection.append(',');
			}
			selection.append(sAllSongsId[i]);
		}
		selection.append(')');		
		Cursor cursor = resolver.query(media, Song.FILLED_PROJECTION, selection.toString(), null, null);
		if(cursor == null) {
			sAllSongsId = null;
			return;
		}
		
		int count = cursor.getCount();
		if(count > 0 ) {
			for (int i = 0; i != count; ++i) {
				cursor.moveToNext();
				Song newSong = new Song(-1);
				newSong.populate(cursor);
				newSong.flags |= Song.FLAG_RANDOM;
				sAllSongsCache[i] = newSong;
			}
		}
		cursor.close();
	}
	
		
	public static long[] queryAllSongs(ContentResolver resolver)
	{
		
		sAllSongsIdx = 0;
		sRandomCacheEnd = -1;

		Uri media = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";
		Cursor cursor = resolver.query(media, Song.EMPTY_PROJECTION, selection, null, null);
		if (cursor == null || cursor.getCount() == 0) {
			sSongCount = 0;
			return null;
		}

		int count = cursor.getCount();
		long[] ids = new long[count];
		for (int i = 0; i != count; ++i) {
			if (!cursor.moveToNext())
				return null;
			ids[i] = cursor.getLong(0);
		}
		sSongCount = count;
		cursor.close();


		return ids;
	}

	@Override
	public void onClick(View v) {
		int vid = v.getId();
		switch(vid) {
		case R.id.musicplayer_bottom_btn:
			Log.i("DEBUG_TAG","MusicPlayer Bottom Btn is click");
			openMusicPlayer();
			break;
		}
		
	}	
	
	public void openMusicPlayer () {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 123);		
	}
	
	
	 @Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		 
		 
        if(resultCode == RESULT_OK && requestCode == 123){
             Uri uriSound=data.getData();
             playSongWithUri(uriSound);
       }
        else {
        	Log.i("DEBUG_TAG", "Error, requestCode = " + requestCode +", resultCode = " + resultCode);
        	
        }
	 }
	 
	 
	public void playSongWithUri(Uri uri) {
		try {
			this.stopSong();
		}
		catch(Exception e ) {
		}
		
		if(mMediaPlayer == null) {
			mMediaPlayer = new MediaPlayer();
		}
        
        try {
			mMediaPlayer.setDataSource(this, uri);
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
        
        try {
			mMediaPlayer.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        mMediaPlayer.start();
	}
	
	
	
	
	public void playSongWithIdx(int idx) {
		Log.i("DEBUG_TAG","playSongWithIdx("+idx+")");
		if(sAllSongsCache == null) {Log.i("DEBUG_TAG","playSongWithIdx("+idx+") but sAllSongsCache is null");}
		if(idx<0 || idx >= sAllSongsCache.length ) {Log.i("DEBUG_TAG","playSongWithIdx("+idx+") but out range of sAllSongsCache bounds");			}
		try {
			this.stopSong();
		}
		catch(Exception e ) {
			
		}
		
		if(mMediaPlayer == null) {
			mMediaPlayer = new MediaPlayer();
		}
		
		try {
			String audioFilePath = sAllSongsCache[idx].path;
			Log.i("DEBUG_TAG","Media Play with Title = "+sAllSongsCache[idx].path+", audioFilePath = " + audioFilePath);
			mMediaPlayer.setDataSource(audioFilePath);
			mMediaPlayer.prepare();
			mMediaPlayer.start();
		} catch(Exception e) {
			Log.e("DEBUG_TAG","Playback failed.",e);
		}
	}
	public void stopSong() {
		if(mMediaPlayer == null) return;
		mMediaPlayer.stop();
		mMediaPlayer.release();
		mMediaPlayer = null;
	}
	 
}
