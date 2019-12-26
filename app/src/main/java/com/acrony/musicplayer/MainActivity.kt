package com.acrony.musicplayer

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Message
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SeekBar

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mp:MediaPlayer
    private var totalTime:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        mp=MediaPlayer.create(this,R.raw.music)

        mp.isLooping=true

        mp.setVolume(0.5f,0.5f)

        totalTime=mp.duration



        volumeBar.setOnSeekBarChangeListener(
            object :SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                   if(fromUser){
                       var volumeNum=progress/100.0f
                       mp.setVolume(volumeNum,volumeNum)
                   }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {


                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {

                }


            }

        )

        //Position Bar
        songpositionBar.max=totalTime
        songpositionBar.setOnSeekBarChangeListener(
            object :SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if(fromUser){
                        mp.seekTo(progress)
                    }

                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {

                }

            }
        )


        Thread(Runnable { while(mp!=null)
        try {
            var msg=Message()
            msg.what=mp.currentPosition
            handler.sendMessage(msg)
            Thread.sleep(1000)
        }
        catch (e:InterruptedException){

        }

        }).start()

    }

    @SuppressLint("HandlerLeak")
    var handler=object:Handler(){
        override fun handleMessage(msg:Message){
            var currentPosition=msg.what

            songpositionBar.progress=currentPosition

            var elapsedT=createTimeLabel(currentPosition)
            elapsedTime.text=elapsedT

           var remainingT=createTimeLabel(totalTime-currentPosition)
           remainingTime.text="-$remainingT"
        }
    }

    fun createTimeLabel(time:Int):String{
        var timeLabel=""

        var min=time/1000/60

        var sec=time/1000%60

        timeLabel="$min:"

        if(sec<10)timeLabel+="0"

        timeLabel=timeLabel+sec

        return timeLabel
    }

    fun playBtnClick(v: View)
    {
        if(mp.isPlaying){
            mp.pause()
            playBtn.setBackgroundColor(R.drawable.stop)
        }
        else
        {
            mp.start()
            playBtn.setBackgroundColor(R.drawable.play)
        }
    }

}
