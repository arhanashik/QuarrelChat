package com.project.blackspider.classschedule.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.blackspider.classschedule.CustomAlertDialogs.CustomAlertDialogBox;
import com.project.blackspider.classschedule.Utils.CustomAnimation;
import com.project.blackspider.classschedule.Utils.CustomButtonPressedStateChanger;
import com.project.blackspider.classschedule.Utils.CustomImageConverter;
import com.project.blackspider.classschedule.DataBase.DBHelper;
import com.project.blackspider.classschedule.FinalClasses.FinalVariables;
import com.project.blackspider.classschedule.Utils.LruBitmapCache;
import com.project.blackspider.classschedule.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Mr blackSpider on 12/23/2016.
 */

public class CustomListAdapterAllBroadcastMessages extends ArrayAdapter<String> implements View.OnClickListener{
    private Activity context;
    private ArrayList<String> allPostmenName = new ArrayList<>();
    private ArrayList<String> allPostmenEmail = new ArrayList<>();
    private ArrayList<String> allPostsSl = new ArrayList<>();
    private ArrayList<String> allPosts = new ArrayList<>();
    private ArrayList<String> allPostsDate = new ArrayList<>();
    private ArrayList<String> myPostsSl = new ArrayList<>();
    private ArrayList<String> postmanAllInfo = new ArrayList<>();

    private View rowView;
    private LinearLayout linearLayoutSinglePost;
    private ImageView imageViewPostmanImage;
    private TextView textViewPostmanName;
    private TextView textViewPostDate;
    private TextView textViewPostMessage;
    private Button buttonEditPost;
    private Button buttonDeletePost;
    private EditText editTextPost;

    private LayoutInflater inflater;

    private ImageLoader imageLoader;
    private Bitmap bitmap;
    private StringRequest stringRequest;
    private RequestQueue requestQueue;

    private AlertDialog alertDialog;
    private AlertDialog.Builder alertDialogBuilder;
    private ProgressDialog pd;

    private FinalVariables finalVariables;
    private CustomImageConverter customImageConverter;
    private CustomAnimation customAnimation;
    private DBHelper dbHelper;
    private CustomAlertDialogBox customAlertDialogBox;
    private CustomButtonPressedStateChanger customButtonPressedStateChanger;

    private static final int FLAG_NEWS_FEED_POST_UPDATE = 0;
    private static final int FLAG_NEWS_FEED_POST_DELETE = 1;
    private static final int FLAG_SCHEDULE_POST_UPDATE = 2;
    private static final int FLAG_SCHEDULE_POST_DELETE = 3;

    private String userInput;

    public CustomListAdapterAllBroadcastMessages(Activity context, ArrayList<String> allPostmenName,
                                                 ArrayList<String> allPostmenEmail,  ArrayList<String> allPosts,
                                                 ArrayList<String> allPostsDate) {
        super(context, R.layout.layout_broadcast_message, allPostmenName);
        // TODO Auto-generated constructor stub
        this.context=context;
        this.allPostmenName = allPostmenName;
        this.allPostmenEmail = allPostmenEmail;
        this.allPosts = allPosts;
        //this.allPostsSl = allPostsSl;
        this.allPostsDate = allPostsDate;
        //this.myPostsSl = myPostsSl;

        dbHelper = new DBHelper(context);
        finalVariables = new FinalVariables();
        customImageConverter = new CustomImageConverter();
        customAnimation = new CustomAnimation(context);
        customAlertDialogBox = new CustomAlertDialogBox(context);
        customButtonPressedStateChanger = new CustomButtonPressedStateChanger(context);
        pd = new ProgressDialog(context);

        requestQueue = Volley.newRequestQueue(context);

        //postmanAllInfo = dbHelper.getUserInfo();
    }

    public View getView(int position, View view, ViewGroup parent) {
        inflater=context.getLayoutInflater();
        rowView=inflater.inflate(R.layout.layout_broadcast_message, null,true);

        int backgrounds[]={R.drawable.rounded_corner,R.drawable.rounded_corner1};

        linearLayoutSinglePost = (LinearLayout) rowView.findViewById(R.id.linearLayoutSinglePost);
        imageViewPostmanImage = (ImageView) rowView.findViewById(R.id.imageViewPostmanImage);
        textViewPostmanName = (TextView) rowView.findViewById(R.id.textViewPostmanName);
        textViewPostDate = (TextView) rowView.findViewById(R.id.textViewPostDate);
        textViewPostMessage = (TextView) rowView.findViewById(R.id.textViewPostMessage);
        buttonEditPost = (Button) rowView.findViewById(R.id.buttonEditPost);
        buttonDeletePost = (Button) rowView.findViewById(R.id.buttonDeletePost);

        linearLayoutSinglePost.setBackgroundResource(backgrounds[position%2]);
        linearLayoutSinglePost.setPadding(20,20,12,20);

        imageViewPostmanImage.setVisibility(View.GONE);
        //setPostmanImage(imageViewPostmanImage, allPostmenImagePath.get(position));
        textViewPostmanName.setText(allPostmenName.get(position));
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(allPostsDate.get(position)),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        textViewPostDate.setText(timeAgo);
        textViewPostMessage.setText(allPosts.get(position));

        //buttonEditPost.setTag(position+"");
        //buttonDeletePost.setTag(position+"");
        //buttonEditPost.setOnClickListener(this);
        //buttonDeletePost.setOnClickListener(this);

//        for (int i = 0; i<myPostsSl.size(); i++){
//            if(allPostsSl.get(position)==myPostsSl.get(i)){
//                buttonEditPost.setVisibility(View.VISIBLE);
//                buttonDeletePost.setVisibility(View.VISIBLE);
//                //Toast.makeText(context, "You have a post!", Toast.LENGTH_SHORT).show();
//                //myPostsSl.remove(i);
//                //break;
//            }
//        }

        return rowView;

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
//        int position = Integer.parseInt(v.getTag().toString());
//        Log.e("Post sl: ",""+allPostsSl.get(position));
//        String session = postmanAllInfo.get(finalVariables.SESSION_INDEX).replace("-","_");
//        String tableName = "table_news_feed_"+postmanAllInfo.get(finalVariables.FACULTY_INDEX)+"_"+session;
//        tableName=tableName.toLowerCase();
//
//        switch (v.getId()){
//            case R.id.buttonEditPost:
//                Log.e("Edit Button: ",""+v.getId());
//                //Toast.makeText(context, "Post SL "+allPostsSl.get(position), Toast.LENGTH_SHORT).show();
//                getNewInput(FLAG_NEWS_FEED_POST_UPDATE, tableName, position);
//                break;
//
//            case R.id.buttonDeletePost:
//                Log.e("Delete Button: ",""+v.getId());
//                //Toast.makeText(context, "Post SL "+allPostsSl.get(position), Toast.LENGTH_SHORT).show();
//                getNewInput(FLAG_NEWS_FEED_POST_DELETE, tableName, position);
//                break;
//
//            default:
//                break;
//        }
    }

    private void setPostmanImage(final ImageView imgView, final String url){
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_loading1);
        bitmap = customImageConverter.getCircledBitmap(bitmap);
        imgView.setImageBitmap(bitmap);
        customAnimation.roll(imgView);

        imageLoader = new ImageLoader(this.requestQueue,
                new LruBitmapCache());
        imageLoader.get(url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {

                if(isImmediate){
                    //Toast.makeText(getApplicationContext(), "Loading image", Toast.LENGTH_SHORT).show();
                }else {
                    //Toast.makeText(getApplicationContext(), "Image loaded", Toast.LENGTH_SHORT).show();
                    bitmap = customImageConverter.getResizedBitmap(response.getBitmap(), 80);
                    bitmap = customImageConverter.getCircledBitmap(bitmap);
                    imgView.clearAnimation();
                    imgView.setImageBitmap(bitmap);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(FinalVariables.TAG, "Volley Error: "+error.getMessage());
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_error);
                bitmap = customImageConverter.getResizedBitmap(bitmap, 80);
                bitmap = customImageConverter.getCircledBitmap(bitmap);
                imgView.clearAnimation();
                imgView.setImageBitmap(bitmap);
            }
        });
    }

    protected void getNewInput(final int flag, final String tableName, final int position){
        String positiveButtonTitle = "Yes";
        // get layout_developer.xml view
        if (flag == FLAG_NEWS_FEED_POST_UPDATE){
            alertDialogBuilder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            rowView = inflater.inflate(R.layout.layout_single_post_input, null);
            editTextPost = (EditText) rowView.findViewById(R.id.editTextPost);
            editTextPost.setText(allPosts.get(position));
            alertDialogBuilder.setView(rowView);
            positiveButtonTitle = "Update";

        }else if(flag == FLAG_NEWS_FEED_POST_DELETE){
            alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setTitle("Are you sure?");
            positiveButtonTitle = "Yes, Delete";

        }

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(positiveButtonTitle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(flag == FLAG_NEWS_FEED_POST_UPDATE){
                            userInput = editTextPost.getText().toString();
                            if(userInput.isEmpty() || userInput.length()>500){
                                Toast.makeText(context, "Invalid Message Length", Toast.LENGTH_SHORT).show();

                            }else {

                                doVolleyRequest(position, FinalVariables.UPDATE_NEWS_FEED_POST_URL, FLAG_NEWS_FEED_POST_UPDATE, tableName);
                            }

                        }else if(flag == FLAG_NEWS_FEED_POST_DELETE) {
                            doVolleyRequest(position, FinalVariables.DELETE_NEWS_FEED_POST_URL, FLAG_NEWS_FEED_POST_DELETE, tableName);

                        }
                    }
                });
        // create alert dialog
        alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();

    }

    private void doVolleyRequest(final int position, String url, final int flag, final String tableName){
        pd = new ProgressDialog(context);
        pd.setMessage("Please wait...");
        pd.setCancelable(false);
        pd.show();

        stringRequest = new StringRequest(
                Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                Log.d("Response: ", "From server: " + response);

                if (response.isEmpty())
                    Toast.makeText(context, "No server response", Toast.LENGTH_SHORT).show();
                else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        handleJSON(position, jsonObject, flag);
                    } catch (JSONException e) {

                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Log.d("Error: ",volleyError.toString());
                Toast.makeText(context, volleyError.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                //Creating parameters
                Map<String,String> params = new Hashtable<>();

                //Adding parameters
                params.put("sl", allPostsSl.get(position));

                if(flag == FLAG_NEWS_FEED_POST_UPDATE){
                    params.put("post", userInput);
                    params.put("table", tableName);

                }else if(flag == FLAG_NEWS_FEED_POST_DELETE){
                    params.put("table", tableName);

                }

                //returning parameters
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    String message = "";
    String success = "";

    private void handleJSON(final int position, JSONObject jsonObject, final int flag){

        try{
            String what = jsonObject.getString(FinalVariables.KEY_SUCCESS);

            if (what.equals(FinalVariables.SUCCESS)) { //means valid id
                message = jsonObject.getString(FinalVariables.KEY_MESSAGE);
                success = FinalVariables.SUCCESS;
                //Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                if(flag== FLAG_NEWS_FEED_POST_UPDATE){
                    allPosts.add(position, userInput);
                    notifyDataSetChanged();
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    message = jsonObject.getString("data");
                    Toast.makeText(context, jsonObject.getString("data"), Toast.LENGTH_SHORT).show();

                }else if(flag== FLAG_NEWS_FEED_POST_DELETE){
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    allPostmenName.remove(position);
                    allPostmenEmail.remove(position);
                    allPostsSl.remove(position);
                    allPostsDate.remove(position);
                    allPosts.remove(position);
                    notifyDataSetChanged();
                }

            }else { //means error
                message = jsonObject.getString(FinalVariables.KEY_MESSAGE);
                success = FinalVariables.FAILURE;
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                message = jsonObject.getString("data");
                Toast.makeText(context, jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
            }
            Log.d("Message: ",message);
            Log.d("Success: ",success);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
