/* 
 * The MIT License
 *
 * Copyright 2016 charleszamora.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ph.com.globe.connect;

import java.util.Map;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;

import org.apache.cordova.CallbackContext;

/**
 * AMAX / Rewards Class.
 * 
 * @author Charles Zamora czamora@openovate.com
 */
public class Amax extends Context {
    /* Amax api url */
    private final String AMAX_URL = "https://devapi.globelabs.com.ph/rewards/v1/transactions/send";
    
    /* API app id */
    protected String appId     = null;
    
    /* API app secret */
    protected String appSecret = null;
    
    /* Rewards token */
    protected String rewardsToken = null;
    
    /* Subscriber address */
    protected String address = null;
    
    /* Defined promo */
    protected String promo = null;
    
    /**
     * Create Amax class without paramters.
     */
    public Amax() {
    }
    
    /**
     * Create Amax class with appId and
     * appSecret parameters.
     * 
     * @param appId app id
     * @param appSecret app secret
     */
    public Amax(String appId, String appSecret) {
        // set app id
        this.appId = appId;
        // set app secret
        this.appSecret = appSecret;
    }
    
    /**
     * Set API app id.
     * 
     * @param  appId app id
     * @return this
     */
    public Amax setAppId(String appId) {
        // set app id
        this.appId = appId;
        
        return this;
    }
    
    /**
     * Set API app secret.
     * 
     * @param appSecret app secret
     * @return this 
     */
    public Amax setAppSecret(String appSecret) {
        // set app secret
        this.appSecret = appSecret;
        
        return this;
    }
    
    /**
     * Set rewards token.
     * 
     * @param  rewardsToken rewards token
     * @return this
     */
    public Amax setRewardsToken(String rewardsToken) {
        // set rewards token
        this.rewardsToken = rewardsToken;
        
        return this;
    }
    
    /**
     * Set subscriber address.
     * 
     * @param  address subscriber address
     * @return this
     */
    public Amax setAddress(String address) {
        // set subscriber address
        this.address = address;
        
        return this;
    }
    
    /**
     * Set defined promo.
     * 
     * @param  promo defined promo
     * @return this
     */
    public Amax setPromo(String promo) {
        // set defined promo
        this.promo = promo;
        
        return this;
    }
    
    /**
     * Send rewards request.
     * 
     * @param  rewardsToken rewards token
     * @param  address subscriber address
     * @param  promo defined promo
     * @param  asyncHandler
     * @return void
     * @throws HttpRequestException http request exception
     * @throws HttpResponseException http response exception
     */
    public void sendRewardRequest(
        String rewardsToken,
        String address,
        String promo,
        AsyncHandler asyncHandler)
        throws HttpRequestException, HttpResponseException {
        
        // set url
        String url = this.AMAX_URL;
        
        // set base data
        Map<String, Object> data = new HashMap<>();
        
        // set outbound reward reqeuest data
        Map<String, Object> orr = new HashMap<>();
        
        // set app id
        orr.put("app_id", this.appId);
        // set app secret
        orr.put("app_secret", this.appSecret);
        // set rewards token
        orr.put("rewards_token", rewardsToken);
        // set address
        orr.put("address", address);
        // set promo
        orr.put("promo", promo);
        
        // set outbound reward request data
        data.put("outboundRewardRequest", new org.json.JSONObject(orr));

        // send request
        new HttpRequest()
        // set url
        .setUrl(url)
        // set data
        .setData(data)
        // set async handler
        .setAsyncHandler(asyncHandler)
        // execute async post
        .execute("post");
    }
    
    /**
     * Send rewards request.
     *
     * @param  asyncHandler
     * @return void
     * @throws HttpRequestException http request exception
     * @throws HttpResponseException http response exception
     */
    public void sendRewardRequest(AsyncHandler asyncHandler)
        throws HttpRequestException, HttpResponseException {
        
        // call send reward request
        this.sendRewardRequest(this.rewardsToken, this.address, this.promo, asyncHandler);
    }

    /**
     * Process the execute command from cordova.
     *
     * @param  action
     * @param  args
     * @param  callbackContext
     * @return boolean
     * @throws JSONException
     */
    @Override
    public boolean execute(
        final String action, 
        JSONArray args, 
        final CallbackContext callbackContext) 
        throws JSONException {

        // set app id?
        if("setAppId".equals(action)) {
            // call set app id
            this.setAppId(args.get(0).toString());

        // set app secret?
        } else if("setAppSecret".equals(action)) {
            // call set app secret
            this.setAppSecret(args.get(0).toString());

        // set rewards token?
        } else if("setRewardsToken".equals(action)) {
            // call set rewards token
            this.setRewardsToken(args.get(0).toString());

        // set address?
        } else if("setAddress".equals(action)) {
            // call set address
            this.setAddress(args.get(0).toString());
        
        // set promo?
        } else if("setPromo".equals(action)) {
            // call set promo
            this.setPromo(args.get(0).toString());
        
        // send reward request?
        } else if("sendRewardRequest".equals(action)) {
            // try building
            try {
                // call send reward request
                this.sendRewardRequest(
                    new AsyncHandler() {
                    @Override
                    public void response(HttpResponse response) throws HttpResponseException {
                        // try parsing
                        try {
                            // send response
                            callbackContext.success(response.getJsonResponse());
                        } catch(HttpResponseException e) {
                            callbackContext.error(e.getMessage());
                        }
                    }
                });
            } catch(HttpRequestException e) {
                callbackContext.error(e.getMessage());
            } catch(HttpResponseException e) {
                callbackContext.error(e.getMessage());
            }
        }

        return true;
    }
}
