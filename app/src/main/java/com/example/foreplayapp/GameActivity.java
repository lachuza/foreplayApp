package com.example.foreplayapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.foreplayapp.game.Game;
import com.example.foreplayapp.game.Player;
import com.example.foreplayapp.game.PlayerActivity;
import com.example.foreplayapp.timer.CircularCounter;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {

    private static int positionXArray[];
    private static int positionYArray[];

    private Game game = null;

    public int height=0;
    public int width=0;
    private String nameMale;
    private String nameFemale;
    ImageView dice_picture; 	//reference to dice picture
    Random rng=new Random();    //generate random numbers
    SoundPool dice_sound;       //For dice sound playing
    int sound_id;		        //Used to control sound stream return by SoundPool
    int alarm_sound_id;
    Handler handler;	        //Post message to start roll
    Timer timer=new Timer();	//Used to implement feedback to user
    boolean rolling=false;		//Is die rolling?


    private CircularCounter meter;
    String[] activitys1Times;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //Our function to initialise sound playing
        InitSound();
        //Get a reference to image widget
        dice_picture = (ImageView) findViewById(R.id.diceView);
        dice_picture.setOnClickListener(new HandleClick());
        //link handler to callback
        handler=new Handler(callback);





    }

    @Override
    protected void onStart() {
        super.onStart();
        nameMale=getIntent().getStringExtra("MALE");
        nameFemale=getIntent().getStringExtra("FEMALE");
        if (game==null){
            game = new Game(nameMale,nameFemale,(ImageView)findViewById(R.id.maleView),(ImageView)findViewById(R.id.femaleView),(TextView)findViewById(R.id.playerMaleText),(TextView)findViewById(R.id.PlayerFemaleText));
        }

        height=getIntent().getIntExtra("HEIGHT",0);
        width=getIntent().getIntExtra("WIDTH",0);

        game.getPlayerFemale().setName(nameFemale);
        game.getPlayerMale().setName(nameMale);
        initPos();
        ImageView image = game.getCurrentPlayer().getImageView();
        image.setX(positionXArray[game.getCurrentPlayer().getPos()]);
        image.setY(positionYArray[game.getCurrentPlayer().getPos()]);
        ImageView image2 = game.getNextPlayer().getImageView();
        image2.setX(positionXArray[game.getNextPlayer().getPos()]);
        image2.setY(positionYArray[game.getNextPlayer().getPos()]);
        updateProgress();
        tooglePlayerTurn();
        activitys1Times=getApplicationContext().getResources().getStringArray(R.array.activity_level1);





    }


    //User pressed dice, lets start
    private class HandleClick implements View.OnClickListener {
        public void onClick(View arg0) {
            if (!rolling) {
                rolling = true;
                //Show rolling image
                dice_picture.setImageResource(R.drawable.dice3droll);
                //Start rolling sound
                dice_sound.play(sound_id, 1.0f, 1.0f, 0, 0, 1.0f);
                //Pause to allow image to update
                timer.schedule(new Roll(), 400);
            }
        }
    }

    //New code to initialise sound playback
    void InitSound() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //Use the newer SoundPool.Builder
            //Set the audio attributes, SONIFICATION is for interaction events
            //uses builder pattern
            AudioAttributes aa = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            //default max streams is 1
            //also uses builder pattern
            dice_sound= new SoundPool.Builder().setAudioAttributes(aa).build();

        } else {
            // Running on device earlier than Lollipop
            //Use the older SoundPool constructor
            dice_sound=PreLollipopSoundPool.NewSoundPool();
        }
        //Load the dice sound
        sound_id=dice_sound.load(this,R.raw.shake_dice,1);
        alarm_sound_id=dice_sound.load(this,R.raw.on_time,1);
    }

    //When pause completed message sent to callback
    class Roll extends TimerTask {
        public void run() {
            handler.sendEmptyMessage(0);
        }
    }

    private void rollDice(){
        //Get roll result
        //Remember nextInt returns 0 to 5 for argument of 6
        //hence + 1
        int rollNumber=rng.nextInt(6)+1;
        //int rollNumber=5;
        switch(rollNumber) {
            case 1:
                dice_picture.setImageResource(R.drawable.one);
                break;
            case 2:
                dice_picture.setImageResource(R.drawable.two);
                break;
            case 3:
                dice_picture.setImageResource(R.drawable.three);
                break;
            case 4:
                dice_picture.setImageResource(R.drawable.four);
                break;
            case 5:
                dice_picture.setImageResource(R.drawable.five);
                break;
            case 6:
                dice_picture.setImageResource(R.drawable.six);
                break;
            default:
        }

        move(rollNumber);
    }

    //Receives message from timer to start dice roll
    Handler.Callback callback = new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            rollDice();
            return true;
        }
    };

    //Clean up
    protected void onPause() {
        super.onPause();
        dice_sound.pause(sound_id);
    }
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

    }

    private void updateProgress(){
        ProgressBar barMale=findViewById(R.id.progressBarMale);
        barMale.setProgress(game.getPlayerMale().getLap());
        ProgressBar barFemale=findViewById(R.id.progressBarFemale);
        barFemale.setProgress(game.getPlayerFemale().getLap());
        TextView levelMaleText=findViewById(R.id.levelMaleText);
        levelMaleText.setText("NIVEL "+game.getPlayerMale().getLap());
        TextView levelFemaleText=findViewById(R.id.levelFemaleText);
        levelFemaleText.setText("NIVEL "+game.getPlayerFemale().getLap());
    }



    private class MyAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationEnd(Animation animation) {
            ImageView image = game.getCurrentPlayer().getImageView();
            image.setX(positionXArray[game.getCurrentPlayer().getPos()]);
            image.setY(positionYArray[game.getCurrentPlayer().getPos()]);
            image.clearAnimation();
            updateProgress();
            doAction();



        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationStart(Animation animation) {
        }

    }

    private class AnimationWithOutActionListener implements Animation.AnimationListener {

        @Override
        public void onAnimationEnd(Animation animation) {
            ImageView image = game.getCurrentPlayer().getImageView();
            image.clearAnimation();
            image.setX(positionXArray[game.getCurrentPlayer().getPos()]);
            image.setY(positionYArray[game.getCurrentPlayer().getPos()]);
            cambiarTurno();
            getApplicationContext().getResources().getString(R.string.app_name);

        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationStart(Animation animation) {
        }

    }

    private void doAction(){
        String action=game.getCurrentPlayer().getNextAction();
        String player=game.getCurrentPlayer().getName();
        switch(action) {
            case "BED":
                showDialog(player,getString(R.string.action_bed),R.drawable.bed);
                break;
            case "DICES":
                showDialog(player,getString(R.string.action_dice),R.drawable.dice);
                break;
            case "PICKUP":
                showDialog(player,String.format(getString(R.string.action_pickup),game.getCurrentPlayer().getName()),R.drawable.pickup);
                break;
            case "PICKUPOTHER":
                showDialog(player,String.format(getString(R.string.action_pickupother),game.getNextPlayer().getName()),R.drawable.pickupother);
                break;
            case "GOBACK":
                showDialog(player,getString(R.string.action_goback),R.drawable.goback);
                break;
            case "GOBACKN":
                showDialog(player,getString(R.string.action_gobackN),R.drawable.gobackn);
                break;
            case "HEARTS":
                showDialog(player,getString(R.string.action_hearts),R.drawable.hearts);
                break;
            default:
                cambiarTurno();
                break;
        }
    }

    private void doFinishAction(){
        String action=game.getCurrentPlayer().getNextAction();
        String player=game.getCurrentPlayer().getName();
        String otherPlayer=game.getNextPlayer().getName();
        switch(action) {
            case "BED":
                cambiarTurno();
                break;
            case "DICES":
                rolling=false;	//user can press again
                break;
            case "PICKUP":
                showActivityDialog(player+": RETO nivel:"+game.getCurrentPlayer().getLap());
                break;
            case "PICKUPOTHER":
                showActivityDialog(otherPlayer+": RETO nivel:"+game.getNextPlayer().getLap());
                break;
            case "GOBACK":
                goBack();
                break;
            case "GOBACKN":
                rolling=false;	//user can press again
                break;
            case "HEARTS":
                showActivityDialog(player+": RETO nivel:"+game.getCurrentPlayer().getLap());
                break;
            default:
        }
    }

    private void doFinishActivity(){
        String otherPlayer=game.getNextPlayer().getName();
        if ("HEARTS".equals(game.getCurrentPlayer().getNextAction())){
            game.getCurrentPlayer().setNextAction("HEARTS2");
            showActivityDialog(otherPlayer+": RETO nivel:"+game.getNextPlayer().getLap());
        }else{
            cambiarTurno();
        }

    }


    private void followImage(){
        final CountDownTimer timer=new CountDownTimer(10000, 250) {

            public void onTick(long millisUntilFinished) {
                ImageView image = findViewById(R.id.maleView);
                System.out.println("X:"+image.getX()+" Y:"+image.getY());
            }

            public void onFinish() {

            }
        }.start();
    }

    public void cambiarTurno(){
        game.toogleTurn();
        rolling=false;	//user can press again
        tooglePlayerTurn();
    }

    private void tooglePlayerTurn(){
        TextView textView=game.getCurrentPlayer().getNameText();
        textView.setTextColor(Color.RED);
        TextView nextView=game.getNextPlayer().getNameText();
        nextView.setTextColor(Color.BLACK);
        textView.clearAnimation();
        nextView.clearAnimation();
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(250); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        textView.startAnimation(anim);


    }


    public void showDialog(String player,String text,int imageId){
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.actiondialog);
        Button dialogButton = (Button) dialog.findViewById(R.id.buttonContinue);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doFinishAction();
                dialog.dismiss();

            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                doFinishAction();
            }
        });
        TextView playerText = dialog.findViewById(R.id.playerTextView);
        playerText.setText(player);
        ImageView image = dialog.findViewById(R.id.imageActionView);
        image.setImageResource(imageId);
        TextView actionText = dialog.findViewById(R.id.actionTextView);
        actionText.setText(text);



        // show it
        dialog.show();
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    finish();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    dialog.cancel();
                    break;
            }
        }
    };



    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Esta seguro que desea salir?").setPositiveButton("Si", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }


    public void showActivityDialog(String accion) {

        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom);
        dialog.setTitle("Actividad");
        final PlayerActivity playerActivity = getPlayerActivity();

        final CountDownTimer timer=new CountDownTimer(playerActivity.getTime()*1000, 1000) {

            public void onTick(long millisUntilFinished) {
                final CircularCounter meterAux = (CircularCounter) dialog.findViewById(R.id.meter);
                int val2=0;
                int val3=0;
                int val1=(int)millisUntilFinished / 1000;
                if(val1>=120){
                    val3=val1-120;
                }
                if(val1>=60){
                    val2=val1-60-val3;
                }

                meterAux.setValues(val1, 0, 0);
            }

            public void onFinish() {
                final CircularCounter meterAux = (CircularCounter) dialog.findViewById(R.id.meter);
                meterAux.setValues(0, 0, 0);
                dice_sound.play(alarm_sound_id, 1.0f, 1.0f, 0, 0, 1.0f);
            }
        };


        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.textActivity);
        text.setText(playerActivity.getText());

        TextView player = (TextView) dialog.findViewById(R.id.textPlayer);
        player.setText(playerActivity.getName()+":");


        ImageView image = (ImageView) dialog.findViewById(R.id.dialogImage);
        image.setBackgroundResource(playerActivity.getImageId());

        CircularCounter meterAux = (CircularCounter) dialog.findViewById(R.id.meter);
        meterAux.setValues(playerActivity.getTime(),0,0);
        meterAux.setRange(playerActivity.getTime());
        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                dialog.dismiss();
                doFinishActivity();
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                timer.cancel();
                doFinishActivity();
            }
        });
        Button dialogStartButton = (Button) dialog.findViewById(R.id.dialogStart);
        if (playerActivity.getTime()==0){
            dialogStartButton.setVisibility(View.GONE);
            meterAux.setVisibility(View.GONE);
        }

        ImageView dialogImage=dialog.findViewById(R.id.dialogImage);
        dialogImage.setBackgroundResource(playerActivity.getImageId());
        //R.drawable.hearts

        // if button is clicked, close the custom dialog
        dialogStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Button startButton=(Button)dialog.findViewById(R.id.dialogStart);
                startButton.setEnabled(false);

                final CircularCounter meterAux = (CircularCounter) dialog.findViewById(R.id.meter);




                //130 segundos
                CountDownTimer start = timer.start();


                Handler activityHandler;

                Runnable r;
                activityHandler = new Handler();
                r = new Runnable(){
                    int total = 131;
                    int val1=0;
                    int val2=0;
                    int val3=0;
                    public void run(){
                        if (total>0){
                            //end
                            total--;
                        }
                        if(total>=120){
                            val3=total-120;
                        }
                        if(total>=60){
                            val2=total-60-val3;
                        }
                        val1=total-val2-val3;


                        meterAux.setValues(total, val2, val3);
                        handler.postDelayed(this, 1000);
                    }
                };
            }
        });








        dialog.show();

    }



    private void moveImage2(Animation.AnimationListener listener,boolean forward){
        ImageView image = game.getCurrentPlayer().getImageView();
        float xi,xf,yi,yf;
        xi =positionXArray[game.getCurrentPlayer().getLastPos()];
        xf =positionXArray[game.getCurrentPlayer().getPos()];
        yi =positionYArray[game.getCurrentPlayer().getLastPos()];
        yf =positionYArray[game.getCurrentPlayer().getPos()];

        if (xi-xf!=0){
            ObjectAnimator animationX = ObjectAnimator.ofFloat(image, "translationX",xf );
            animationX.setDuration(1000);
            animationX.start();
        }
        if (yi-yf!=0){
            ObjectAnimator animationY = ObjectAnimator.ofFloat(image, "translationY",yf );
            animationY.setDuration(1000);
            animationY.start();
        }
        cambiarTurno();


    }

    private void moveImage(Animation.AnimationListener listener,boolean forward){
        int delay=500;
        ImageView image = game.getCurrentPlayer().getImageView();
        float inix=image.getX();
        float iniy=image.getY();
        AnimationSet animationSet = new AnimationSet(false);
        int c=0;
        int dir=1;
        if (!forward){dir=-1;}
        boolean finishTrip=false;
        int i=game.getCurrentPlayer().getLastPos();
        while (!finishTrip){
            if(forward){
                i++;
            }else{i--;}
            if (i<0){i=32;}
            if (i>31){i=0;}





            if (i!=game.getCurrentPlayer().getPos()){
                TranslateAnimation animation = new TranslateAnimation(0, positionXArray[i]-inix,0, positionYArray[i]-iniy);
                animation.setDuration(delay);
                animation.setStartOffset(delay*c);
                inix=positionXArray[i];
                iniy=positionYArray[i];
                animationSet.addAnimation(animation);
                c=c+1;
            }else{finishTrip=true;}
        }



        TranslateAnimation animation = new TranslateAnimation(0, positionXArray[game.getCurrentPlayer().getPos()]-inix,0, positionYArray[game.getCurrentPlayer().getPos()]-iniy);
        animation.setDuration(delay);
        animation.setStartOffset(delay*c);
        animation.setAnimationListener(listener);
        animationSet.setFillEnabled(true);
        animationSet.setFillAfter(true);
        animationSet.addAnimation(animation);
        image.startAnimation(animationSet);
    }

    private void goBack(){
        game.goBack();
        moveImage(new AnimationWithOutActionListener(),false);
    }


    private void move(int moveCount ){
        if ("GOBACKN".equals(game.getCurrentPlayer().getNextAction())){
            game.goBackN(moveCount);
            moveImage(new MyAnimationListener(),false);
        }else{
            game.play(moveCount);
            moveImage(new MyAnimationListener(),true);
        }

}

private PlayerActivity getPlayerActivity(){
        int lap=game.getCurrentPlayer().getLap();
        boolean currentPlayer=true;
    String[] activitys=null;
    String[] activitysTime=null;
    PlayerActivity playerActivity = new PlayerActivity();
    playerActivity.setImageId(R.drawable.hearts);
    if ("PICKUPOTHER".equals(game.getCurrentPlayer().getNextAction())||"HEARTS2".equals(game.getCurrentPlayer().getNextAction())){
        currentPlayer=false;
    }
    if (!currentPlayer){
        lap=game.getNextPlayer().getLap();
    }

    int i=game.getNextActivity();
    playerActivity.setId(i);
    if (lap==1){
        activitys=getApplicationContext().getResources().getStringArray(R.array.activity_level1);
        activitysTime=getApplicationContext().getResources().getStringArray(R.array.activity_level1_times);

    }
    if (lap==2){
        activitys=getApplicationContext().getResources().getStringArray(R.array.activity_level2);
        activitysTime=getApplicationContext().getResources().getStringArray(R.array.activity_level2_times);

    }
    if (lap==3){
        Player auxP = game.getCurrentPlayer();
        if (!currentPlayer){
            auxP=game.getNextPlayer();
        }

        if ("MALE".equals(auxP.getSex())){
            activitys=getApplicationContext().getResources().getStringArray(R.array.activity_level3_male);
            activitysTime=getApplicationContext().getResources().getStringArray(R.array.activity_level3_male_times);
        }else {
            activitys = getApplicationContext().getResources().getStringArray(R.array.activity_level3_female);
            activitysTime=getApplicationContext().getResources().getStringArray(R.array.activity_level3_female_times);
        }

    }
    if (lap==4){
        Player auxP = game.getCurrentPlayer();
        if (!currentPlayer){
            auxP=game.getNextPlayer();
        }

        if ("MALE".equals(auxP.getSex())){
            activitys=getApplicationContext().getResources().getStringArray(R.array.activity_level4_male);
            activitysTime=getApplicationContext().getResources().getStringArray(R.array.activity_level4_male_times);
        }else {
            activitys = getApplicationContext().getResources().getStringArray(R.array.activity_level4_female);
            activitysTime=getApplicationContext().getResources().getStringArray(R.array.activity_level4_female_times);
        }
        playerActivity.setImageId(getImageId(i,auxP.getSex()));
    }

    if (!currentPlayer){
        playerActivity.setName(game.getNextPlayer().getName());
        playerActivity.setText(String.format(activitys[i], game.getCurrentPlayer().getName(), game.getNextPlayer().getName()));
    }else{
        playerActivity.setName(game.getCurrentPlayer().getName());
        playerActivity.setText(String.format(activitys[i], game.getNextPlayer().getName(), game.getCurrentPlayer().getName()));
    }
    playerActivity.setTime(Integer.parseInt(activitysTime[i]));
    return playerActivity;

}

    private int getImageId(int pos,String sex) {
        int id=0;
        switch (pos+1){
            case 3: if ("MALE".equals(sex)){id=R.drawable.kama_male03;}else{id=R.drawable.kama_female03;}break;
            case 4: if ("MALE".equals(sex)){id=R.drawable.kama_male04;}else{id=R.drawable.kama_female04;}break;
            case 7: if ("MALE".equals(sex)){id=R.drawable.kama_male07;}else{id=R.drawable.kama_female07;}break;
            case 8: if ("MALE".equals(sex)){id=R.drawable.kama_male08;}else{id=R.drawable.kama_female08;}break;
            case 10: id=R.drawable.kama_69;break;
            default: id=R.drawable.hearts;
        }
        return  id;


    }

    private void initPos(){

        ConstraintLayout layout = findViewById(R.id.layout);
        int h = height;
        int w = width;
        int ax = ((w / 8)/2);
        int ay = (h/10)/2;

        int diffX = ( ((w / 8) ));
        int diffY = h/10;
        int maxH = (h/ 12) * 10;
        int maxW = (w / 12) * 4;

        ImageView image = findViewById(R.id.femaleView);
        int ix=image.getLayoutParams().height/2;
        int iy=image.getLayoutParams().width/2;
        ax=ax-ix;
        ay=ay-iy;


        // Log.i(" x position ",Integer.toString(diffX));
        //Log.i(" x position ",Integer.toString(diffY));

        //position array calculating..........
        positionXArray = new int[32];

        positionXArray[0] = ax;
        positionXArray[1] = ax+ (1*diffX);
        positionXArray[2] = ax+ (2*diffX);
        positionXArray[3] = ax+ (3*diffX);
        positionXArray[4] = ax+ (4*diffX);
        positionXArray[5] = ax+ (5*diffX);
        positionXArray[6] = ax+ (6*diffX);
        positionXArray[7] = ax+ (7*diffX);
        positionXArray[8] = ax+ (7*diffX);
        positionXArray[9] = ax+ (7*diffX);
        positionXArray[10] = ax+ (7*diffX);
        positionXArray[11] = ax+ (7*diffX);
        positionXArray[12] = ax+ (7*diffX);
        positionXArray[13] = ax+ (7*diffX);
        positionXArray[14] = ax+ (7*diffX);
        positionXArray[15] = ax+ (7*diffX);
        positionXArray[16] = ax+ (7*diffX);
        positionXArray[17] = ax+ (6*diffX);
        positionXArray[18] = ax+ (5*diffX);
        positionXArray[19] = ax+ (4*diffX);
        positionXArray[20] = ax+ (3*diffX);
        positionXArray[21] = ax+ (2*diffX);
        positionXArray[22] = ax+ (1*diffX);
        positionXArray[23] = ax;
        positionXArray[24] = ax;
        positionXArray[25] = ax;
        positionXArray[26] = ax;
        positionXArray[27] = ax;
        positionXArray[28] = ax;
        positionXArray[29] = ax;
        positionXArray[30] = ax;
        positionXArray[31] = ax;

        positionYArray = new int[32];

        positionYArray[0] = ay;
        positionYArray[1] = ay;
        positionYArray[2] = ay;
        positionYArray[3] = ay;
        positionYArray[4] = ay;
        positionYArray[5] = ay;
        positionYArray[6] = ay;
        positionYArray[7] = ay;
        positionYArray[8] = ay + (1*diffY);
        positionYArray[9] = ay + (2*diffY);
        positionYArray[10] = ay + (3*diffY);
        positionYArray[11] = ay + (4*diffY);
        positionYArray[12] = ay + (5*diffY);
        positionYArray[13] = ay + (6*diffY);
        positionYArray[14] = ay + (7*diffY);
        positionYArray[15] = ay + (8*diffY);
        positionYArray[16] = ay + (9*diffY);
        positionYArray[17] = ay + (9*diffY);
        positionYArray[18] = ay + (9*diffY);
        positionYArray[19] = ay + (9*diffY);
        positionYArray[20] = ay + (9*diffY);
        positionYArray[21] = ay + (9*diffY);
        positionYArray[22] = ay + (9*diffY);
        positionYArray[23] = ay + (9*diffY);
        positionYArray[24] = ay + (8*diffY);
        positionYArray[25] = ay + (7*diffY);
        positionYArray[26] = ay + (6*diffY);
        positionYArray[27] = ay + (5*diffY);
        positionYArray[28] = ay + (4*diffY);
        positionYArray[29] = ay + (3*diffY);
        positionYArray[30] = ay + (2*diffY);
        positionYArray[31] = ay + (1*diffY);

        image.setX(positionXArray[0]);
        image.setY(positionYArray[0]);
        ImageView imageMale = findViewById(R.id.maleView);
        imageMale.setX(positionXArray[0]);
        imageMale.setY(positionYArray[0]);



    }
}
