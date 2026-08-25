import { google } from "googleapis";

const PACKAGE_NAME="com.odelly.pypet";
// Allow both the original Treasure Trove catalog and the newer premium catalog.
// The server remains fail-closed: an unknown product can never be granted.
const ALLOWED_PRODUCTS=new Set([
  "treasure_little","treasure_shiny","treasure_rare","treasure_royal","treasure_legendary","treasure_mythic","treasure_skyrealm","treasure_deepsea","treasure_woodland","treasure_stargazer","treasure_dragon","treasure_space","treasure_maker","treasure_storybook","treasure_festival","treasure_crystal",
  "pypet_premium_cozy_home","pypet_premium_garden","pypet_premium_pet_outfits","pypet_premium_music","pypet_premium_treehouse","pypet_premium_dragon_lair","pypet_premium_space_station","pypet_premium_arcade","pypet_premium_world_expansion","pypet_premium_royal_estate","pypet_premium_creator_bundle","pypet_premium_ultimate_world"
]);
function json(res,status,body){res.status(status).setHeader("Content-Type","application/json").send(JSON.stringify(body));}
function getCredentials(){const raw=process.env.GOOGLE_PLAY_SERVICE_ACCOUNT_JSON;if(!raw)throw new Error("GOOGLE_PLAY_SERVICE_ACCOUNT_JSON is not configured");return JSON.parse(raw);}
export default async function handler(req,res){
  if(req.method!=="POST"){res.setHeader("Allow","POST");return json(res,405,{verified:false,error:"method_not_allowed"});}
  try{
    const {packageName,productId,purchaseToken}=req.body||{};
    if(packageName!==PACKAGE_NAME||!ALLOWED_PRODUCTS.has(productId)||typeof purchaseToken!=="string"||purchaseToken.length<10)return json(res,400,{verified:false,error:"invalid_purchase_request"});
    const credentials=getCredentials();const auth=new google.auth.GoogleAuth({credentials,scopes:["https://www.googleapis.com/auth/androidpublisher"]});const androidpublisher=google.androidpublisher({version:"v3",auth});
    const result=await androidpublisher.purchases.products.get({packageName:PACKAGE_NAME,productId,token:purchaseToken});const purchase=result.data;const verified=purchase.purchaseState===0;
    return json(res,200,{verified,productId,purchaseState:purchase.purchaseState??null,acknowledgementState:purchase.acknowledgementState??null,orderId:purchase.orderId??null});
  }catch(error){console.error("purchase verification failed",error?.message||error);return json(res,200,{verified:false,error:"verification_failed"});}
}
