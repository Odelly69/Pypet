package com.odelly.pypet;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import com.android.billingclient.api.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;

/** Google Play Billing client. Production entitlement is granted only after backend verification. */
public final class TreasurePurchaseManager implements BillingClient.PurchasesUpdatedListener {
    public interface Listener { void onProductsReady(List<ProductDetails> products); void onPurchaseGranted(String productId); void onMessage(String message); }
    private static final String PREFS="pypet_treasure_purchases";
    private final BillingClient billing; private final SharedPreferences prefs; private final Map<String,ProductDetails> products=new HashMap<>(); private Listener listener;
    private final String verifyUrl;

    public TreasurePurchaseManager(Context context){
        prefs=context.getSharedPreferences(PREFS,Context.MODE_PRIVATE);
        verifyUrl=context.getString(com.odelly.pypet.R.string.purchase_verification_url);
        billing=BillingClient.newBuilder(context).enablePendingPurchases().setListener(this).build();
    }
    public void connect(Listener l){listener=l;if(billing.isReady()){queryProducts();return;}billing.startConnection(new BillingClientStateListener(){
        public void onBillingSetupFinished(BillingResult r){if(r.getResponseCode()==BillingClient.BillingResponseCode.OK)queryProducts();else message("Google Play Billing unavailable: "+r.getDebugMessage());}
        public void onBillingServiceDisconnected(){message("Google Play Billing disconnected. Try the store again.");}
    });}
    private void queryProducts(){List<QueryProductDetailsParams.Product> ids=new ArrayList<>();for(TreasureCatalog.Item i:TreasureCatalog.all())ids.add(QueryProductDetailsParams.Product.newBuilder().setProductId(i.productId).setProductType(BillingClient.ProductType.INAPP).build());
        billing.queryProductDetailsAsync(QueryProductDetailsParams.newBuilder().setProductList(ids).build(),(r,d)->{if(r.getResponseCode()!=BillingClient.BillingResponseCode.OK){message("Could not load Treasure Trove: "+r.getDebugMessage());return;}products.clear();for(ProductDetails p:d.getProductDetailsList())products.put(p.getProductId(),p);if(listener!=null)listener.onProductsReady(new ArrayList<>(products.values()));restorePurchases();});}
    public void buy(Activity a,String id){ProductDetails d=products.get(id);if(d==null){message("That Treasure is not currently available.");return;}BillingFlowParams.ProductDetailsParams p=BillingFlowParams.ProductDetailsParams.newBuilder().setProductDetails(d).build();BillingResult r=billing.launchBillingFlow(a,BillingFlowParams.newBuilder().setProductDetailsParamsList(Collections.singletonList(p)).build());if(r.getResponseCode()!=BillingClient.BillingResponseCode.OK)message("Google Play could not start the purchase: "+r.getDebugMessage());}
    public void restorePurchases(){if(!billing.isReady())return;billing.queryPurchasesAsync(QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.INAPP).build(),(r,ps)->{if(r.getResponseCode()==BillingClient.BillingResponseCode.OK&&ps!=null)for(Purchase p:ps)handlePurchase(p);});}
    public void onPurchasesUpdated(BillingResult r,List<Purchase> ps){if(r.getResponseCode()==BillingClient.BillingResponseCode.OK&&ps!=null)for(Purchase p:ps)handlePurchase(p);else if(r.getResponseCode()!=BillingClient.BillingResponseCode.USER_CANCELED)message("Purchase was not completed: "+r.getDebugMessage());}
    private void handlePurchase(Purchase p){if(p.getPurchaseState()!=Purchase.PurchaseState.PURCHASED){if(p.getPurchaseState()==Purchase.PurchaseState.PENDING)message("Purchase pending. Google Play will notify us when it completes.");return;}for(String id:p.getProducts())verifyThenGrant(p,id);}
    private void verifyThenGrant(Purchase p,String id){
        new AsyncTask<Void,Void,Boolean>(){protected Boolean doInBackground(Void...v){try{return verify(p,id);}catch(Exception e){return false;}}
            protected void onPostExecute(Boolean ok){if(!ok){message("We couldn't verify that purchase yet. Your Treasure has not been granted; please try Restore Purchases later.");return;}grant(id);if(!p.isAcknowledged())billing.acknowledgePurchase(AcknowledgePurchaseParams.newBuilder().setPurchaseToken(p.getPurchaseToken()).build(),r->{if(r.getResponseCode()!=BillingClient.BillingResponseCode.OK)message("Purchase acknowledgement will be retried.");});}}
        }.execute();
    }
    private boolean verify(Purchase p,String id)throws Exception{
        if(verifyUrl==null||verifyUrl.trim().isEmpty())return false;
        HttpURLConnection c=(HttpURLConnection)new URL(verifyUrl).openConnection();c.setRequestMethod("POST");c.setConnectTimeout(8000);c.setReadTimeout(10000);c.setDoOutput(true);c.setRequestProperty("Content-Type","application/json");
        JSONObject body=new JSONObject();body.put("packageName","com.odelly.pypet");body.put("productId",id);body.put("purchaseToken",p.getPurchaseToken());
        try(OutputStream out=c.getOutputStream()){out.write(body.toString().getBytes("UTF-8"));}
        if(c.getResponseCode()!=200)return false;try(InputStream in=c.getInputStream()){String s=new BufferedReader(new InputStreamReader(in)).lines().reduce("",(a,b)->a+b);JSONObject result=new JSONObject(s);return result.optBoolean("verified",false)&&id.equals(result.optString("productId"));}
    }
    private void grant(String id){if(!prefs.getBoolean(id,false)){prefs.edit().putBoolean(id,true).apply();if(listener!=null)listener.onPurchaseGranted(id);}}
    public boolean owns(String id){return prefs.getBoolean(id,false);}private void message(String s){if(listener!=null)listener.onMessage(s);}
}
