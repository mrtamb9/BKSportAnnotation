define("newflag",["require","jquery-1"],function(b,e){var a='<span class="icon-new">New</span>',d=60*60*1000;var c={init:function(){e.each(e("a"),function(j,k){var g=e(k).data("published");if(g){if(bbc.fmtj.page.serverTime-g<d){var h=e(k),f=e(a);if(!h.hasClass("live")&&!h.hasClass("live-updating")){if(h.hasClass("kink")){h.find(".headline-wrapper").prepend(f)}else{if(h.hasClass("image")){h.find(".image-wrapper").after(f)}else{if(h.children().hasClass("icon-gvl3")&&!h.hasClass("live")){h.find(".icon-gvl3").after(f)}else{if(!h.hasClass("live")){h.prepend(f)}}}}}}}})}};c.init()});