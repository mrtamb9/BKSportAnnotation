(function(){commentsModule=function(){this.events={};this.params={comment_max_lines:5,items_per_page:20,sort_order:"Ascending",filter:"none",current_page:1,prefix:"",parent_uri:"",is_authorised:false,textarea_height:"40px",character_count:0,textareaLock:false,limitingCharacters:false,critical_error_msg:"We're having some problems displaying the comments at the moment. Sorry. We're doing our best to fix it"};this.flags={ready:false,glow_ready:false,filter_tabs_events_added:false,submit_box_events_added:false,postOk:true,speedBumping:false,setupComplete:false};this.translations={you:"You",your:"Your",comments:"Comments",your_comments:"Your comments",editors_pick:"Editors' Pick"};this.message_types=["error","message"];var a=this;this.container={};this.loadingElements={};this.tabElements={};this.initParams=function(c){for(var b in c){this.params[b]=c[b]}if(this.params.ajax_load_url&&this.params.bbc_id_env&&this.params.ajax_preview_url&&this.params.items_per_page&&this.params.sort_order&&this.params.site_name&&this.params.forum_id){a.flags.ready=true;a.init()}};this.initGlow=function(b){a.glow=b;if(a.glow.isReady){a.flags.glow_ready=true}};this.initLogger=function(b){a.logger=b;a.flags.logger_ready=true};this.setRootNode=function(c){for(var d in c){var b=a.glow.dom.get(c[d]);if(b.length>0){this.container=b;this.rootSelector=c[d];break}}};this.findNode=function(c){var b=a.container.get(c);return(b.length>0)?b:a.glow.dom.get(c)};this.init=function(){this.accountForOldTextarea();if(this.flags.setupComplete){this.loadingElements=this.findNode(".dna-tab-loading, .dna-loading");this.attachFormEvents();this.personaliseCommentFeed()}else{this.setRootNode([".dna-comments_module"]);this.loadTranslationsFile();this.loadingElements=this.findNode(".dna-tab-loading, .dna-loading");this.postingElements=this.findNode(".dna-tab-posting");this.tabElements=this.findNode("ul.tabs li");this.commentsList=this.findNode("div.dna-comment-list");this.messageElement=this.findNode(".comments-message");this.formElements=this.findNode(" form.postcomment input, form.postcomment textarea");this.formSubmitElements=this.findNode(" form.postcomment input");this.previewElement=this.findNode(".dna-comment-preview");this.attachFilterEventsToLinks();this.attachPostCommentLinkEvent();this.attachInitialViewCommentsLink();this.attachAnonCommentingEventsToLinks();this.findNode("form.postcomment input.dna-commentbox-preview").css("display","inline");if(typeof(identity)=="object"){identity.status.enableJavaScript();identity.addSigninHandler(function(){a.setLoggedIn();a.loadLoggedInFragment();a.accountForOldTextarea()})}if(location.protocol=="https:"){this.params.ajax_load_url=this.params.ajax_load_url.replace("http:","https:")}this.flags.setupComplete=true}this.enhanceCommentFeed();this.attachPaginatorEventsToLinks();this.attachSortOrderEventsToLinks();this.updateCommentCounts(a.params.comment_count);this.attachCharacterLimitToSubmitForm()};this.addTranslations=function(b){if(typeof(b)=="object"){this.translations=b;return true}return false};this.loadTranslationsFile=function(){a.glow.net.loadScript(a.params.translations_url+a.params.loc+"/",{useCache:true})};this.getDefaultSortOrder=function(){return(a.params.preset=="opinion")?"Descending":"Ascending"};this.accountForOldTextarea=function(){var b=a.glow.dom.get("div.dna-comments_module textarea#dna-commentbox-text");if(b.length>0&&b.parent().hasClass("dna-textarea-container")==false){var e=a.glow.dom.get("form.dna-commentbox");b.remove();var d=a.glow.dom.create('<div class="dna-textarea-container"><textarea id="dna-commentbox-text" name="comment"></textarea></div>');e.prepend(d);a.glow.dom.get("div.dna-comments_module div.dna-textarea-container").css("margin-top","24px")}var c=a.glow.dom.get("li.dna-comment span.vcard a");c.attr("href","#");a.glow.events.addListener(c,"click",function(){return false})};this.enhanceCommentFeed=function(){a.findNode(".dna-comments-comment-footer, .dna-rate-label").css("visibility","hidden");a.findNode("ul.collections li.dna-comment").each(function(){a.glow.events.addListener(this,"mouseover",function(){a.glow.dom.get(this).get(".dna-comments-comment-footer").css("visibility","visible")},this);a.glow.events.addListener(this,"mouseout",function(){a.glow.dom.get(this).get(".dna-comments-comment-footer").css("visibility","hidden")},this)});var f=a.findNode("ul.collections li.dna-comment form.dna-rate-comment .dna-rate-controls");var e=a.findNode("ul.collections li.dna-comment .ie6-7 form.dna-rate-comment .dna-rate-controls");var d=a.findNode("ul.collections li.notables form.dna-rate-comment .dna-rate-controls");var b=a.findNode("ul.collections li.dna-comment form.dna-rate-comment .dna-rate-label");if(!this.params.rtl){f.css({position:"relative",visibility:"visible"});b.css({position:"relative"})}else{f.css({position:"relative",visibility:"visible"});b.css({position:"relative"});e.css({position:"absolute",left:"0"})}d.css("visibility","hidden");a.findNode("ul.collections li.dna-comment form.dna-rate-comment").each(function(){a.glow.events.addListener(this,"mouseover",function(){a.glow.dom.get(this).get(".dna-rate-label").css("visibility","visible")},this);a.glow.events.addListener(this,"mouseout",function(){a.glow.dom.get(this).get(".dna-rate-label").css("visibility","hidden")},this)});a.updateAllCsrfTokensFromCommentForm();a.params.preLoadImages={};a.findNode("ul.collections li.dna-comment form.dna-rate-comment .dna-rate-controls input").each(function(){a.glow.events.addListener(this,"click",function(){var g=a.glow.dom.get(this);g.parent().parent().parent().get("input.direction").val(g.val())},this)});a.findNode("ul.collections li.dna-comment form.dna-rate-comment").each(function(){a.glow.events.addListener(this,"submit",function(){var h=a.glow.dom.get(this);var g=a.findNode(".loading img").attr("src");h.get(".dna-rate-label").html('<img src="'+g+'" style="position: absolute; left: -22px;" class="dna-comments-all-loaders rate-loader" />').show();a.glow.events.removeAllListeners(this);a.glow.events.addListener(this,"submit",function(){return false});a.glow.net.loadScript(h.attr("action")+"?"+a.glow.data.encodeUrl(h.val())+"&viaAjax=true");return false})});for(var c in a.params.preLoadImages){a.params.preLoadImages[c].image=new Image();a.params.preLoadImages[c].image.src=c}};this.personaliseCommentFeed=function(){if(a.params.logged_in&&typeof(a.params.user_id)=="number"){a.findNode("div.dna-comment-list .comment_username_"+a.params.user_id).each(function(){a.glow.dom.get(this).html(a.translations.you)});a.findNode("div.dna-comment-list .comment_moderated_username_"+a.params.user_id).each(function(){a.glow.dom.get(this).html(a.translations.your)});a.findNode("div.dna-comment-list p.comment_complain_"+a.params.user_id).each(function(){a.glow.dom.get(this).remove()});if(a.params.dml||a.params.del){a.findNode("div.dna-comment-list .dna-comments-comment-footer").each(function(){if(a.params.dml){var c=a.glow.dom.get(this).parent().attr("id").replace("comment_","");var e='<p class="flag"><a href="'+a.params.ml+c+'" class="popup" target="_blank">'+a.translations.moderation_history+"</a></p>";e+=(a.glow.dom.get(this).children().hasClass("flag"))?"<br />":"";a.glow.dom.get(this).prepend(e)}if(a.params.del){var c=a.glow.dom.get(this).parent().attr("id").replace("comment_","");switch(a.params.filter){case"EditorPicks":var d=a.params.dep;var b=a.translations.remove_editors_pick;break;default:var d=a.params.mep;var b=a.translations.make_editors_pick;break}var e='<p class="flag"><a href="'+d+c+"&siteId="+a.params.site_name+'" class="editor_pick_link">'+b+"</a></p>";e+=(a.glow.dom.get(this).children().hasClass("flag"))?"<br />":"";a.glow.dom.get(this).prepend(e);a.glow.events.addListener(a.glow.dom.get(this).get(".editor_pick_link"),"click",a.setEditorsPick,this)}})}}};this.populateCommentCountAnchors=function(){var b=(typeof(a.params.comment_count)=="number"&&a.params.comment_count>0)?'<span class="dna-comment-count-number">('+a.params.comment_count+")</span>":'<span class="dna-comment-count-number"></span>';var c=a.glow.dom.get(".dna-comment-count-simple");var e=a.glow.dom.get(".dna-comment-count-personal");var d=a.glow.dom.get(".dna-comment-anchor");c.html('<a href="#dna-comments">'+a.translations.comments+" "+b+' <span class="gvl3-icon gvl3-icon-comment"></span></a> ');e.html('<a href="#dna-comments">'+a.translations.your_comments+" "+b+' <span class="gvl3-icon gvl3-icon-comment"></span></a> ');d.html('<a href="#dna-comments">'+a.translations.add_your_comment+"</a> ");a.glow.events.addListener(c,"click",a.scrollTop,this);a.glow.events.addListener(e,"click",a.scrollTop,this);a.glow.events.addListener(d,"click",a.scrollTop,this)};this.setLoggedIn=function(){this.findNode(".dna-commentbox-logged-in").show();this.findNode(".dna-commentbox-logged-out").hide();this.attachFormEvents();this.formatSubmitCommentForm();this.enableForm()};this.loadLoggedInFragment=function(){var b=a.findNode(".loading img").attr("src");var d='<img src="'+b+'" style="margin-bottom: -2px; margin-right: 3px;" /> '+a.translations.loading_user_details;a.findNode("div.dna-logged-in-fragment").html(d);var c="?preset="+a.params.preset+"&ptrt="+escape(a.params.parent_uri)+"&idenv="+escape(a.params.bbc_id_env)+"&siteId="+a.params.site_name+"&loc="+a.params.loc+"&forumId="+a.params.forum_id+"&title="+a.params.title+"&policy_uri="+a.params.policy_uri;this.glow.net.loadScript(this.params.base_non_secure_url+"fragment/userfragment/"+c)};this.attachCharacterLimitToSubmitForm=function(){if(a.params.character_limit>0){a.params.limitingCharacters=true;var b=a.glow.dom.get("#dna-commentbox-text");if(b.length>0){a.limitedFormInput=b;a.glow.events.addListener(b,"keyup",a.limitText,this)}}};this.limitText=function(){if(a.params.character_limit>0){this.addNotificationPadding();var b=a.limitedFormInput.val().length;if(b>a.params.character_limit){a.disableFormInputs();a.notifyOverCharacterLimit(b-a.params.character_limit)}else{if(a.formSubmitElements.hasAttr("disabled")){a.enableFormInputs()}a.notifyCharacterCount(b)}}};this.notifyCharacterCount=function(c){var b=a.params.character_limit-c;b=(b!=0)?b:"0";a.showPosting(a.getTranslation("char_remaining_limit",{char_count:b}));a.postingElements.removeClass("dna-warning")};this.notifyOverCharacterLimit=function(b){var c=b;a.showPosting(a.getTranslation("char_over_limit",{char_count:c}));a.postingElements.addClass("dna-warning")};this.setEditorsPick=function(c){var b=a.findNode(".loading img");a.glow.dom.get(c.source).prepend('<img src="'+b.attr("src")+'" style="position: absolute; margin-left: -24px" class="mep" /> ');a.glow.net.loadScript(c.source.href);return false};this.attachInitialViewCommentsLink=function(){var b=a.findNode("a.dna-initial-view-more-comments");if(b.length>0){a.glow.events.removeAllListeners(b);a.glow.events.addListener(b,"click",function(){a.loadPage(1,false,null,null,true);return false},this)}};this.openJsPopupWindow=function(b){window.open(b.source.href,"dna_popup_window","height = 615, width = 694, scrollbars = 1");return false};this.attachPaginatorEventsToLinks=function(){var b=a.findNode("ul.comments_pagination_ul li a");b.each(function(){var c=a.glow.dom.get(this).attr("href").match(/comments_page=(\d*)/);if(c){a.glow.events.addListener(this,"click",function(){a.loadPage(c[1],this,function(){a.scrollTop()});return false})}})};this.attachSortOrderEventsToLinks=function(){var b=a.findNode("ul li.dna-rate-order-controls a");b.each(function(){var c=a.glow.dom.get(this).attr("href").match(/sortOrder=(\w*)/);if(c&&typeof(c)=="object"){a.glow.events.addListener(this,"click",function(){a.params.sort_by="RatingValue";a.params.sort_order=c[1];a.loadPage(1,this,function(){a.scrollTop()});return false})}var d=this.href.match(/filter=([a-zA-Z]*)/);if(d&&typeof(d)=="object"){a.glow.events.addListener(this,"click",function(){a.params.sort_by="Created";a.params.sort_order=a.getDefaultSortOrder();a.params.filter=d[1];a.loadPage(1,this);return false})}})};this.attachFilterEventsToLinks=function(){if(!this.params.filter_tabs_events_added){this.tabElements.get("a").each(function(){var b=this.href.match(/filter=([a-zA-Z]*)/);if(b&&typeof(b)=="object"){a.glow.events.addListener(this,"click",function(){a.params.sort_by="Created";a.params.sort_order=a.getDefaultSortOrder();a.params.filter=b[1];a.loadPage(1,this,function(){a.setTab(b[1])});return false})}var c=this.href.match(/sortBy=([a-zA-Z]*)/);if(c&&typeof(c)=="object"){a.glow.events.addListener(this,"click",function(){a.params.filter="none";a.params.sort_by=c[1];a.loadPage(1,this,function(){a.setTab(c[1])});return false})}});this.params.filter_tabs_events_added=true}};this.attachFormEvents=function(){if(!this.flags.submit_box_events_added){var b=this.findNode("form.postcomment");if(a.params.allow_not_signed_in_commenting=="1"&&typeof(a.params.user_id)!="number"){this.glow.events.addListener(b,"submit",function(){a.displayRecaptcha();return false})}else{this.glow.events.addListener(b,"submit",function(){a.postComment();return false})}this.glow.events.addListener(this.findNode("textarea#dna-commentbox-text, form.postcomment .anon_displayname"),"focus",this.prepareSubmitBoxForSubmit);this.glow.events.addListener(this.findNode("textarea#dna-commentbox-text, form.postcomment .anon_displayname"),"blur",this.checkFormOkToFormat,this);this.glow.events.addListener(this.findNode("input.dna-commentbox-preview"),"click",this.previewComment);this.glow.events.addListener(this.findNode("a.popup"),"click",this.openJsPopupWindow);this.formatSubmitCommentForm();this.flags.submit_box_events_added=true}};this.checkUserAuthedToComment=function(){if(!a.params.is_authorised){var b=escape(a.params.policy_uri);a.glow.net.loadScript(a.params.user_auth_url+"?p="+b,{useCache:false})}};this.attachPostCommentLinkEvent=function(){var b=this.findNode(".dna-commentbox-add-comment-cta");if(b.length>0){this.glow.events.addListener(b,"click",function(){var c=a.glow.dom.get("textarea#dna-commentbox-text");a.scrollTextarea();c[0].focus();return false})}};this.checkFormOkToFormat=function(){setTimeout(a.formatSubmitCommentForm,750)};this.submitBoxCanBeClosed=function(){if(a.params.textareaLock){return false}var c=a.findNode(".anon_displayname");if(c.length>0){if(c.val()!=""){return false}}var b=a.findNode("form.postcomment textarea#dna-commentbox-text");if(b.length>0){var d=b.val();if(d.length>0&&d!=a.translations.add_your_comment_dots){return false}}return true};this.formatSubmitCommentForm=function(){var b=a.findNode("form.postcomment textarea#dna-commentbox-text");if(b.length<1){return false}var c=parseInt(b.parent().css("width"))-((2*parseInt(b.css("padding-left")))+2);b.css("width",c);if(a.submitBoxCanBeClosed()){var b=a.findNode("form.postcomment textarea#dna-commentbox-text");b.addClass("secondary_body");b.css("font-weight","bold");b.val(a.translations.add_your_comment_dots);b.css("height",a.params.textarea_height);a.removeNotificationPadding();a.hidePosting();a.previewElement.html("");a.previewElement.hide();a.findNode("form.postcomment .dna-comments-footer, form.postcomment .anon_displayname_holder").hide()}else{b.removeClass("secondary_body");b.addClass("primary_body");b.css("font-weight","normal");b.css("height","80px");a.addNotificationPadding();a.findNode(".dna-comments-footer, .anon_displayname_holder").show();return true}};this.refreshTextarea=function(){var b=a.glow.dom.get("form.postcomment textarea#dna-commentbox-text");if(b.length>0){var c=b.val();if(c.length>0&&c.toLowerCase()!=a.translations.add_your_comment_dots){setTimeout(function(){b[0].focus();a.limitText()},10)}}};this.prepareSubmitBoxForSubmit=function(){var b=a.findNode("form.postcomment textarea#dna-commentbox-text");var c=b.val();if(c==a.translations.add_your_comment_dots){b.val("");b.removeClass("secondary_body");b.addClass("primary_body");b.css("font-weight","normal");b.css("height","80px");if(a.params.limitingCharacters){a.addNotificationPadding()}a.findNode(".dna-comments-footer, .anon_displayname_holder").show()}else{a.formatSubmitCommentForm()}};this.loadPage=function(e,d,h,c,b){a.showLoader();var b=false;var f={siteId:this.params.site_name,forumId:this.params.forum_id,title:this.params.title,sortOrder:this.params.sort_order,sortBy:this.params.sort_by,limit:this.params.items_per_page,comments_page:e,filter:this.params.filter,preset:this.params.preset,parentUri:this.params.parent_uri,loc:this.params.loc,timezone:this.params.timezone,viaAjax:true};if(c){f.postId=c}var g=a.glow.data.encodeUrl(f);this.glow.net.loadScript(this.params.ajax_load_url+"?"+g,{useCache:b});if(d){d.blur()}return false};this.isValidToPost=function(){if(a.textareaIsReadyToPost()&&a.userIsNotBeingSpeedBumped()&&a.anonDisplayNameReadyToPost()&&a.captchaIsReadyToPost()){a.hideMessage();return true}return false};this.isValidToPreview=function(){if(a.textareaIsReadyToPost()&&a.anonDisplayNameReadyToPost()&&a.captchaIsReadyToPost()){a.hideMessage();return true}return false};this.textareaIsReadyToPost=function(){var b=a.findNode("#dna-commentbox-text").val().replace(/^\s*/,"").replace(/\s*$/,"");if(b.length==0){a.showError(a.translations.you_must_enter_text);return false}return true};this.captchaIsBeingDisplayed=function(){var b=a.findNode("#recaptcha_response_field");if(b.length==0){return false}return b};this.captchaIsReadyToPost=function(){var c=a.captchaIsBeingDisplayed();if(!c){return true}var b=c.val().replace(/^\s*/,"").replace(/\s*$/,"");if(b.length==0){a.showError(a.translations.you_must_complete_captcha);return false}return true};this.anonDisplayNameReadyToPost=function(){var b=a.findNode("form.postcomment input.anon_displayname");if(b.length==0){return true}var c=b.val().replace(/^\s*/,"").replace(/\s*$/,"");if(c.length==0){a.showError(a.translations.you_must_enter_displayname);return false}else{if(c.length>a.params.anon_display_name_max_length){a.showError(a.translations.your_display_name_is_too_long);return false}}return true};this.attachAnonCommentingEventsToLinks=function(){var b=a.findNode(".dna-anon-commenting-link-show");a.glow.events.removeAllListeners(b);a.glow.events.addListener(b,"click",a.showAnonCommentingElements,this);var c=a.findNode(".dna-anon-commenting-link-hide");a.glow.events.removeAllListeners(c);a.glow.events.addListener(c,"click",a.hideAnonCommentingElements,this);var d=a.findNode("form.postcomment input.anon_displayname, form.postcomment textarea#dna-commentbox-text");a.glow.events.removeAllListeners(d);a.glow.events.addListener(d,"focus",function(){a.params.textareaLock=true},this);a.glow.events.addListener(d,"focus",this.prepareSubmitBoxForSubmit,this);a.glow.events.addListener(d,"blur",this.checkFormOkToFormat,this);a.glow.events.addListener(d,"blur",function(){a.params.textareaLock=false},this);return true};this.showAnonCommentingElements=function(){a.findNode(".dna-anon-commenting-show").show();a.findNode(".dna-anon-commenting-hide").hide();a.formatSubmitCommentForm();a.attachAnonCommentingEventsToLinks();a.attachCharacterLimitToSubmitForm();a.scrollTextarea();return false};this.hideAnonCommentingElements=function(){a.findNode(".dna-anon-commenting-show").hide();a.findNode(".dna-anon-commenting-hide").show();a.attachAnonCommentingEventsToLinks();return false};this.userIsNotBeingSpeedBumped=function(){if(a.flags.postOk==false&&a.flags.speedBumping==true){var b=a.getSpeedBumpCountdown();var c=a.getTranslation("speed_bumping",{post_freq:a.params.postFreq,seconds_remaining:b});a.showError(c);return false}return true};this.displayRecaptcha=function(){if(!a.isValidToPost()){return false}a.hideMessage();var b=a.captchaIsBeingDisplayed();if(!b){Recaptcha.create(a.params.recaptcha_public_key,"recaptcha",{theme:"clean",callback:Recaptcha.focus_response_field})}else{this.postComment()}};this.destroyRecaptcha=function(){var b=a.captchaIsBeingDisplayed();if(b){Recaptcha.destroy();b.hide()}};this.resetCaptcha=function(){if(a.captchaIsBeingDisplayed()){Recaptcha.reload()}};this.postComment=function(){if(a.isValidToPost()){var d=a.findNode("form.postcomment");var b=d.val();a.params.sort_by="Created";a.params.sort_order=a.getDefaultSortOrder();b.sortBy=a.params.sort_by;b.sortOrder=a.params.sort_order;b.limit=a.params.items_per_page;b.filter="none";b.preset=a.params.preset;b.allow_not_signed_in_commenting=a.params.allow_not_signed_in_commenting;b.viaAjax=true;a.disableForm();a.showPosting(a.translations.posting_your_comment,true);var c=a.glow.data.encodeUrl(b);a.glow.net.loadScript(d.attr("action")+"?"+c);return false}return true};this.previewComment=function(){if(a.isValidToPreview()){var b=(a.params.num_comments)?a.params.num_comments+1:1;var e=a.findNode("form.postcomment");var c=e.val();c.id=b;c.preset=a.params.preset;c.viaAjax=true;a.showPosting(a.translations.generating_preview,true);var d=a.glow.data.encodeUrl(c);a.glow.net.loadScript(a.params.ajax_preview_url+"?"+d+"&loc="+a.params.loc)}return false};this.stripslashes=function(b){b=b.replace(/\\'/g,"'");b=b.replace(/\\"/g,'"');b=b.replace(/\\0/g,"\0");b=b.replace(/\\\\/g,"\\");return b};this.updateAllCsrfTokensFromCommentForm=function(){this.updateCsrf(this.getCsrf())};this.getCsrf=function(){var b=a.findNode("form#submit_new_comment input.dna-comments-csrf");return(b.length>0)?b.val():""};this.updateCsrf=function(b){var c=a.findNode("input.dna-comments-csrf");if(c.length>0){c.val(b)}};this.scrollToElement=function(c,b){var d=b||10;this.glow.dom.get(window).scrollTop(c.offset().top-d);return false};this.scrollTop=function(){this.scrollToElement(this.container);return false};this.scrollTopError=function(){this.scrollToElement(this.findNode(".comments-message"));return false};this.scrollTextarea=function(){var b=this.findNode("#dna-commentbox-text");this.scrollToElement(b,80);b[0].focus();return false};this.showMessage=function(e,d){d=d||"message";for(var b in a.message_types){var c="comments-message-"+a.message_types[b];if(a.messageElement.hasClass(c)&&a.message_types[b]!=d){a.messageElement.removeClass(c)}}a.messageElement.addClass("comments-message-"+d);a.messageElement.show();a.messageElement.html(a.stripslashes(e));a.hideLoader();a.hidePosting();a.enableFormInputs();a.scrollTopError();a.findNode(".dna-comments-all-loaders").hide();return true};this.showError=function(b){if(typeof(b)!="string"){b=a.params.critical_error_msg}a.enableForm();a.resetCaptcha();return this.showMessage(b,"error")};this.hideMessage=function(){if(!a.messageElement.hasClass("sticky")){a.messageElement.html();a.messageElement.hide();return true}return false};this.showLoader=function(){this.loadingElements.css("display","inline")};this.hideLoader=function(){this.loadingElements.css("display","none")};this.showPosting=function(c,b){this.postingElements.get("span").html(c);if(b==true){this.postingElements.get("img").css("display","inline")}else{this.postingElements.get("img").css("display","none")}this.postingElements.css("display","block")};this.hidePosting=function(){this.postingElements.css("display","none")};this.disableFormInputs=function(){this.formSubmitElements.attr("disabled","true")};this.enableFormInputs=function(){this.formSubmitElements.removeAttr("disabled")};this.disableForm=function(){this.formElements.attr("disabled","true")};this.enableForm=function(){this.formElements.removeAttr("disabled")};this.setTab=function(b){var c=new String(b);this.tabElements.removeClass("sel");this.tabElements.parent().get("#tab_"+c.toLowerCase()).addClass("sel")};this.setSpeedBump=function(){a.params.speedBumpTimeLeft=a.params.postFreq;a.flags.postOk=false;a.flags.speedBumping=true;a.speedBumpTime=setInterval(function(){a.params.speedBumpTimeLeft--;var b=a.findNode("span.speed-bump-countdown");if(b){b.html(a.params.speedBumpTimeLeft)}if(a.params.speedBumpTimeLeft==0){clearInterval(a.speedBumpTime);a.flags.postOk=true;a.flags.speedBumping=false;a.hideMessage()}},1000)};this.getTranslation=function(j,c){if(typeof(a.translations[j])!="undefined"){var k=a.translations[j];var g=/{(\w*)}/g;var e=k.match(g);if(e){for(var d in e){var l=e[d].toString();var h=l.replace(/[\{\}]/g,"");var b=(c[h])?c[h].toString():"";var f=new RegExp(e[d],"g");k=k.replace(f,b)}}return k}};this.updateCommentCounts=function(b){if(b>0){a.glow.dom.get("body .dna-comment-count-number").html("("+b+")")}};this.getSpeedBumpCountdown=function(){return'<span class="speed-bump-countdown">'+a.params.speedBumpTimeLeft+"</span>"};this.addNotificationPadding=function(){if(a.limitedFormInput&&a.limitedFormInput.parent()){a.limitedFormInput.parent().css("padding-bottom","28px")}};this.removeNotificationPadding=function(){if(a.limitedFormInput&&a.limitedFormInput.parent()){a.limitedFormInput.parent().css("padding-bottom","8px")}};this.removeLastScriptNode=function(){var b=comments.glow.dom.get("script");b[b.length-1].parentNode.removeChild(b[b.length-1])}}}());var comments;(function(){gloader.load(["glow","1","glow.events","glow.dom","glow.widgets"],{async:true,onLoad:function(a){a.ready(function(){window.comments=new commentsModule();window.comments.initGlow(a);try{require(["istats-1"],function(c){window.comments.initLogger(c)})}catch(b){}if(typeof(window.commentsParams)=="object"){window.comments.initParams(window.commentsParams);try{delete (window.commentsParams)}catch(b){}}})}})})();