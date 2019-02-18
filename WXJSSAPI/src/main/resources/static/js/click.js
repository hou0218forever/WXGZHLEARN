document.querySelector('#initWX').onclick = function () {
	  var thisPageUrl = location.href.split('#')[0];
		var json = {
			url : thisPageUrl
		};
		$.ajax({
	      type:"POST",
	      url:thisPageUrl,
	      dataType:"json",
	      //data: JSON.stringify({ "url": url}),  
	      success:function(data){
	    	  configWeiXin(data.appId,data.timestamp,data.nonceStr,data.signature);
	      },
	      error:function(data){
	      	console.log("Error: "+data.status);
	      }
	  }); 
  };
  function configWeiXin(appId, timestamp, nonceStr, signature) {
	  wx.config({
	      debug: true,
	      appId: appId,
	      timestamp: timestamp,
	      nonceStr: nonceStr,
	      signature: signature, 
	      jsApiList: [
	        'checkJsApi',
	        'onMenuShareTimeline',
	        'onMenuShareAppMessage',
	        'onMenuShareQQ',
	        'onMenuShareWeibo',
	        'onMenuShareQZone',
	        'hideMenuItems',
	        'showMenuItems',
	        'hideAllNonBaseMenuItem',
	        'showAllNonBaseMenuItem',
	        'translateVoice',
	        'startRecord',
	        'stopRecord',
	        'onVoiceRecordEnd',
	        'playVoice',
	        'onVoicePlayEnd',
	        'pauseVoice',
	        'stopVoice',
	        'uploadVoice',
	        'downloadVoice',
	        'chooseImage',
	        'previewImage',
	        'uploadImage',
	        'downloadImage',
	        'getNetworkType',
	        'openLocation',
	        'getLocation',
	        'hideOptionMenu',
	        'showOptionMenu',
	        'closeWindow',
	        'scanQRCode',
	        'chooseWXPay',
	        'openProductSpecificView',
	        'addCard',
	        'chooseCard',
	        'openCard'
	      ]
	  });
	}