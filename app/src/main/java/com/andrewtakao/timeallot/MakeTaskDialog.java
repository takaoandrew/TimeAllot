package com.andrewtakao.timeallot;

import android.app.DialogFragment;
import android.os.Build;
import android.support.annotation.RequiresApi;

/**
 * Created by andrewtakao on 4/10/17.
 */

@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class MakeTaskDialog extends DialogFragment {

//    OnMyDialogResult mDialogResult;
//    public static EditText durationText;
//    int mDuration;
//
//    public interface NoticeDialogListener {
//        public void onDialogPositiveClick(DialogFragment dialog);
//        public void onDialogNegativeClick(DialogFragment dialog);
//    }
//
//    // Use this instance of the interface to deliver action events
//    NoticeDialogListener mListener;
//
//    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        // Verify that the host activity implements the callback interface
//        try {
//            // Instantiate the NoticeDialogListener so we can send events to the host
//            mListener = (NoticeDialogListener) activity;
//        } catch (ClassCastException e) {
//            // The activity doesn't implement the interface, throw exception
//            throw new ClassCastException(activity.toString()
//                    + " must implement NoticeDialogListener");
//        }
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        // Use the Builder class for convenient dialog construction
//        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//        final View dialogView = inflater.inflate(R.layout.dialog_make_task, null);
//        durationText = (EditText) dialogView.findViewById(R.id.duration);
//
////        int duration = Integer.parseInt(durationText.getText().toString());
//
//        // Inflate and set the layout for the dialog
//        // Pass null as the parent view because its going in the dialog layout
//        builder.setView(dialogView)
//                // Add action buttons
//                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//                        mListener.onDialogPositiveClick(MakeTaskDialog.this);
//                        if( mDialogResult != null ){
//                            mDialogResult.finish(String.valueOf(durationText.getText()));
//                        }
//                        final EditText durationText = (EditText) dialogView.findViewById(R.id.duration);
////                        Log.d("testing", durationText.getText().toString());
//                        mDuration = Integer.parseInt(durationText.getText().toString());
//
////                        int duration = Integer.parseInt((durationText.getText()).toString());
//
//                    }
//                })
//                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        MakeTaskDialog.this.getDialog().cancel();
//                    }
//                });
//        return builder.create();
//    }
//
//    public void setDialogResult(OnMyDialogResult dialogResult){
//        mDialogResult = dialogResult;
//    }
//
//    public interface OnMyDialogResult{
//        void finish(String result);
//    }
}
