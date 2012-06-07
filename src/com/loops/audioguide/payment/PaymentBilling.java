package com.loops.audioguide.payment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import com.loops.audioguide.R;
import com.loops.audioguide.payment.BillingService.RequestPurchase;
import com.loops.audioguide.payment.BillingService.RestoreTransactions;
import com.loops.audioguide.payment.Consts.PurchaseState;
import com.loops.audioguide.payment.Consts.ResponseCode;

public class PaymentBilling extends Activity {

	private static final int DIALOG_CANNOT_CONNECT_ID = 1;
	private static final int DIALOG_BILLING_NOT_SUPPORTED_ID = 2;

	private String mPayloadContents = null;
	private AudioGuidePurchaseObs mAudioGuidePurchaseObserver;
	private Handler mHandler;
	private BillingService mBillingService;
	private static final String TAG = "BillingService";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		init();
	}

	public void init() {
		mAudioGuidePurchaseObserver = new AudioGuidePurchaseObs(mHandler);
		mBillingService = new BillingService();
		mBillingService.setContext(this);
		ResponseHandler.register(mAudioGuidePurchaseObserver);
		if (!mBillingService.checkBillingSupported()) {
			showDialog(DIALOG_CANNOT_CONNECT_ID);
		}
	}

	@Override
	protected void onStart() {
		ResponseHandler.register(mAudioGuidePurchaseObserver);
		super.onStart();
	}

	@Override
	protected void onDestroy() {
		mBillingService.unbind();
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		ResponseHandler.unregister(mAudioGuidePurchaseObserver);
		super.onStop();
	}

	private class AudioGuidePurchaseObs extends PurchaseObserver {

		public AudioGuidePurchaseObs(Handler handler) {
			super(PaymentBilling.this, handler);
		}

		@Override
		public void onBillingSupported(boolean supported) {
			if (Consts.DEBUG) {
				Log.i("", "supported: " + supported);
			}
			if (supported) {

			} else {
				showDialog(DIALOG_BILLING_NOT_SUPPORTED_ID);
			}
		}

		@Override
		public void onPurchaseStateChange(PurchaseState purchaseState,
				String itemId, int quantity, long purchaseTime,
				String developerPayload) {

			if (Consts.DEBUG) {
				Log.i("", "onPurchaseStateChange() itemId: " + itemId + " "
						+ purchaseState);
			}

			if (developerPayload == null) {
				Log.i("", itemId + purchaseState.toString());
			} else {
				Log.i("", itemId + " " + purchaseState + "\n\t"
						+ developerPayload);
			}

			if (purchaseState == PurchaseState.PURCHASED) {
				Log.i("", itemId + " " + purchaseState + "\n\t"
						+ developerPayload);
			}

		}

		@Override
		public void onRequestPurchaseResponse(RequestPurchase request,
				ResponseCode responseCode) {
			if (Consts.DEBUG) {
				Log.d(TAG, request.mProductId + ": " + responseCode);
			}
			if (responseCode == ResponseCode.RESULT_OK) {
				if (Consts.DEBUG) {
					Log.i(TAG, "purchase was successfully sent to server");
				}
				Log.i(request.mProductId, "sending purchase request");
			} else if (responseCode == ResponseCode.RESULT_USER_CANCELED) {
				if (Consts.DEBUG) {
					Log.i(TAG, "user canceled purchase");
				}
				Log.i(request.mProductId, "dismissed purchase dialog");
			} else {
				if (Consts.DEBUG) {
					Log.i(TAG, "purchase failed");
				}
				Log.i(request.mProductId, "request purchase returned "
						+ responseCode);
			}
		}

		@Override
		public void onRestoreTransactionsResponse(RestoreTransactions request,
				ResponseCode responseCode) {

			if (responseCode == ResponseCode.RESULT_OK) {
				if (Consts.DEBUG) {
					Log.d(TAG, "completed RestoreTransactions request");
				}

			} else {
				if (Consts.DEBUG) {
					Log.d(TAG, "RestoreTransactions error: " + responseCode);
				}
			}
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_CANNOT_CONNECT_ID:
			return createDialog(R.string.cannot_connect_title,
					R.string.cannot_connect_message);
		case DIALOG_BILLING_NOT_SUPPORTED_ID:
			return createDialog(R.string.billing_not_supported_title,
					R.string.billing_not_supported_message);
		default:
			return null;
		}
	}

	private Dialog createDialog(int titleId, int messageId) {
		String helpUrl = getString(R.string.help_url);
		if (Consts.DEBUG) {
			Log.i(TAG, helpUrl);
		}
		final Uri helpUri = Uri.parse(helpUrl);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(titleId)
				.setIcon(android.R.drawable.stat_sys_warning)
				.setMessage(messageId)
				.setCancelable(false)
				.setPositiveButton(android.R.string.ok, null)
				.setNegativeButton(R.string.learn_more,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = new Intent(Intent.ACTION_VIEW,
										helpUri);
								startActivity(intent);
							}
						});
		return builder.create();
	}

	public void processpayment() {
		if (Consts.DEBUG) {
			Log.d(TAG, "buying: " + "audio_guide_001" + " sku: "
					+ "audio_guide_001");
		}
		if (!mBillingService.requestPurchase("audio_guide_001",
				mPayloadContents)) {
			showDialog(DIALOG_BILLING_NOT_SUPPORTED_ID);
		}
		mBillingService.restoreTransactions();
	}

}
