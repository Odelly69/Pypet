import { google } from 'googleapis';

const PRODUCTS = new Set([
  'treasure_little','treasure_shiny','treasure_rare','treasure_royal','treasure_legendary','treasure_mythic','treasure_skyrealm','treasure_deepsea','treasure_woodland','treasure_stargazer','treasure_dragon','treasure_space','treasure_maker','treasure_storybook','treasure_festival','treasure_crystal',
  'pypet_premium_cozy_home','pypet_premium_garden','pypet_premium_pet_outfits','pypet_premium_music','pypet_premium_treehouse','pypet_premium_dragon_lair','pypet_premium_space_station','pypet_premium_arcade','pypet_premium_world_expansion','pypet_premium_royal_estate','pypet_premium_creator_bundle','pypet_premium_ultimate_world'
]);
function json(res,status,body){res.status(status).setHeader('Cache-Control','no-store').json(body);}
function serviceAccount(){const raw=process.env.GOOGLE_PLAY_SERVICE_ACCOUNT_JSON;if(!raw)throw new Error('GOOGLE_PLAY_SERVICE_ACCOUNT_JSON is not configured');return JSON.parse(raw);}
export default async function handler(req,res){
  if(req.method!=='POST')return json(res,405,{error:'method_not_allowed'});
  const {packageName,productId,purchaseToken}=req.body||{};
  if(packageName!=='com.odelly.pypet'||!PRODUCTS.has(productId)||typeof purchaseToken!=='string'||purchaseToken.length<20)return json(res,400,{error:'invalid_purchase_request'});
  try{
    const auth=new google.auth.GoogleAuth({credentials:serviceAccount(),scopes:['https://www.googleapis.com/auth/androidpublisher']});
    const client=await auth.getClient();const androidpublisher=google.androidpublisher({version:'v3',auth:client});
    const result=await androidpublisher.purchases.products.get({packageName,productId,token:purchaseToken});const purchase=result.data;
    if(purchase.purchaseState!==0||purchase.consumptionState===1)return json(res,409,{verified:false,error:'purchase_not_eligible'});
    if(purchase.acknowledgementState!==1)await androidpublisher.purchases.products.acknowledge({packageName,productId,token:purchaseToken,requestBody:{developerPayload:'pypet-treasure'}});
    return json(res,200,{verified:true,productId,orderId:purchase.orderId||null});
  }catch(error){console.error('Google Play verification failed',error);return json(res,502,{error:'verification_unavailable'});}
}
