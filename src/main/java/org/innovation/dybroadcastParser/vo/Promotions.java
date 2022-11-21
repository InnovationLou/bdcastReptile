/**
  * Copyright 2022 jb51.net 
  */
package org.innovation.dybroadcastParser.vo;
import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Auto-generated: 2022-11-19 16:5:5
 *
 * @author jb51.net (i@jb51.net)
 * @website http://tools.jb51.net/code/json2javabean
 */
public class Promotions {

    private Activity activity;
    @JSONField(name="apply_coupon")
    private int applyCoupon;
    @JSONField(name="biz_kind")
    private int bizKind;
    @JSONField(name="button_label")
    private String buttonLabel;
    private boolean campaign;
    @JSONField(name="campaign_type")
    private int campaignType;
    @JSONField(name="can_sold")
    private boolean canSold;
    private String cover;
    @JSONField(name="detail_url")
    private String detailUrl;
    @JSONField(name="directly_jump_url")
    private String directlyJumpUrl;
    @JSONField(name="discount_label")
    private List<DiscountLabel> discountLabel;
    @JSONField(name="discount_price")
    private DiscountPrice discountPrice;
    @JSONField(name="elastic_title")
    private String elasticTitle;
    @JSONField(name="event_param")
    private EventParam eventParam;
    private Extra extra;
    @JSONField(name="flash_icon")
    private String flashIcon;
    @JSONField(name="flash_type")
    private int flashType;
    @JSONField(name="hot_atmosphere")
    private HotAtmosphere hotAtmosphere;
    @JSONField(name="in_stock")
    private boolean inStock;
    private int index;
    @JSONField(name="is_campaign")
    private boolean isCampaign;
    @JSONField(name="is_sole_sku")
    private boolean isSoleSku;
    @JSONField(name="item_type")
    private int itemType;
    @JSONField(name="jump_destination")
    private JumpDestination jumpDestination;
    @JSONField(name="max_price")
    private int maxPrice;
    @JSONField(name="min_price")
    private int minPrice;
    @JSONField(name="min_price_str")
    private String minPriceStr;
    @JSONField(name="order_url")
    private String orderUrl;
    private int platform;
    @JSONField(name="price_header")
    private String priceHeader;
    @JSONField(name="product_id")
    private String productId;
    @JSONField(name="product_tag")
    private List<String> productTag;
    @JSONField(name="promotion_id")
    private String promotionId;
    @JSONField(name="regular_price")
    private int regularPrice;
    @JSONField(name="rit_tags")
    private RitTags ritTags;
    @JSONField(name="shop_id")
    private int shopId;
    @JSONField(name="small_cover")
    private String smallCover;
    @JSONField(name="small_pop_card")
    private SmallPopCard smallPopCard;
    private int status;
    @JSONField(name="status_img_map")
    private StatusImgMap statusImgMap;
    @JSONField(name="stock_num")
    private int stockNum;
    private String title;
    public void setActivity(Activity activity) {
         this.activity = activity;
     }
     public Activity getActivity() {
         return activity;
     }

    public void setApplyCoupon(int applyCoupon) {
         this.applyCoupon = applyCoupon;
     }
     public int getApplyCoupon() {
         return applyCoupon;
     }

    public void setBizKind(int bizKind) {
         this.bizKind = bizKind;
     }
     public int getBizKind() {
         return bizKind;
     }

    public void setButtonLabel(String buttonLabel) {
         this.buttonLabel = buttonLabel;
     }
     public String getButtonLabel() {
         return buttonLabel;
     }

    public void setCampaign(boolean campaign) {
         this.campaign = campaign;
     }
     public boolean getCampaign() {
         return campaign;
     }

    public void setCampaignType(int campaignType) {
         this.campaignType = campaignType;
     }
     public int getCampaignType() {
         return campaignType;
     }

    public void setCanSold(boolean canSold) {
         this.canSold = canSold;
     }
     public boolean getCanSold() {
         return canSold;
     }

    public void setCover(String cover) {
         this.cover = cover;
     }
     public String getCover() {
         return cover;
     }

    public void setDetailUrl(String detailUrl) {
         this.detailUrl = detailUrl;
     }
     public String getDetailUrl() {
         return detailUrl;
     }

    public void setDirectlyJumpUrl(String directlyJumpUrl) {
         this.directlyJumpUrl = directlyJumpUrl;
     }
     public String getDirectlyJumpUrl() {
         return directlyJumpUrl;
     }

    public void setDiscountLabel(List<DiscountLabel> discountLabel) {
         this.discountLabel = discountLabel;
     }
     public List<DiscountLabel> getDiscountLabel() {
         return discountLabel;
     }

    public void setDiscountPrice(DiscountPrice discountPrice) {
         this.discountPrice = discountPrice;
     }
     public DiscountPrice getDiscountPrice() {
         return discountPrice;
     }

    public void setElasticTitle(String elasticTitle) {
         this.elasticTitle = elasticTitle;
     }
     public String getElasticTitle() {
         return elasticTitle;
     }

    public void setEventParam(EventParam eventParam) {
         this.eventParam = eventParam;
     }
     public EventParam getEventParam() {
         return eventParam;
     }

    public void setExtra(Extra extra) {
         this.extra = extra;
     }
     public Extra getExtra() {
         return extra;
     }

    public void setFlashIcon(String flashIcon) {
         this.flashIcon = flashIcon;
     }
     public String getFlashIcon() {
         return flashIcon;
     }

    public void setFlashType(int flashType) {
         this.flashType = flashType;
     }
     public int getFlashType() {
         return flashType;
     }

    public void setHotAtmosphere(HotAtmosphere hotAtmosphere) {
         this.hotAtmosphere = hotAtmosphere;
     }
     public HotAtmosphere getHotAtmosphere() {
         return hotAtmosphere;
     }

    public void setInStock(boolean inStock) {
         this.inStock = inStock;
     }
     public boolean getInStock() {
         return inStock;
     }

    public void setIndex(int index) {
         this.index = index;
     }
     public int getIndex() {
         return index;
     }

    public void setIsCampaign(boolean isCampaign) {
         this.isCampaign = isCampaign;
     }
     public boolean getIsCampaign() {
         return isCampaign;
     }

    public void setIsSoleSku(boolean isSoleSku) {
         this.isSoleSku = isSoleSku;
     }
     public boolean getIsSoleSku() {
         return isSoleSku;
     }

    public void setItemType(int itemType) {
         this.itemType = itemType;
     }
     public int getItemType() {
         return itemType;
     }

    public void setJumpDestination(JumpDestination jumpDestination) {
         this.jumpDestination = jumpDestination;
     }
     public JumpDestination getJumpDestination() {
         return jumpDestination;
     }

    public void setMaxPrice(int maxPrice) {
         this.maxPrice = maxPrice;
     }
     public int getMaxPrice() {
         return maxPrice;
     }

    public void setMinPrice(int minPrice) {
         this.minPrice = minPrice;
     }
     public int getMinPrice() {
         return minPrice;
     }

    public void setMinPriceStr(String minPriceStr) {
         this.minPriceStr = minPriceStr;
     }
     public String getMinPriceStr() {
         return minPriceStr;
     }

    public void setOrderUrl(String orderUrl) {
         this.orderUrl = orderUrl;
     }
     public String getOrderUrl() {
         return orderUrl;
     }

    public void setPlatform(int platform) {
         this.platform = platform;
     }
     public int getPlatform() {
         return platform;
     }

    public void setPriceHeader(String priceHeader) {
         this.priceHeader = priceHeader;
     }
     public String getPriceHeader() {
         return priceHeader;
     }

    public void setProductId(String productId) {
         this.productId = productId;
     }
     public String getProductId() {
         return productId;
     }

    public void setProductTag(List<String> productTag) {
         this.productTag = productTag;
     }
     public List<String> getProductTag() {
         return productTag;
     }

    public void setPromotionId(String promotionId) {
         this.promotionId = promotionId;
     }
     public String getPromotionId() {
         return promotionId;
     }

    public void setRegularPrice(int regularPrice) {
         this.regularPrice = regularPrice;
     }
     public int getRegularPrice() {
         return regularPrice;
     }

    public void setRitTags(RitTags ritTags) {
         this.ritTags = ritTags;
     }
     public RitTags getRitTags() {
         return ritTags;
     }

    public void setShopId(int shopId) {
         this.shopId = shopId;
     }
     public int getShopId() {
         return shopId;
     }

    public void setSmallCover(String smallCover) {
         this.smallCover = smallCover;
     }
     public String getSmallCover() {
         return smallCover;
     }

    public void setSmallPopCard(SmallPopCard smallPopCard) {
         this.smallPopCard = smallPopCard;
     }
     public SmallPopCard getSmallPopCard() {
         return smallPopCard;
     }

    public void setStatus(int status) {
         this.status = status;
     }
     public int getStatus() {
         return status;
     }

    public void setStatusImgMap(StatusImgMap statusImgMap) {
         this.statusImgMap = statusImgMap;
     }
     public StatusImgMap getStatusImgMap() {
         return statusImgMap;
     }

    public void setStockNum(int stockNum) {
         this.stockNum = stockNum;
     }
     public int getStockNum() {
         return stockNum;
     }

    public void setTitle(String title) {
         this.title = title;
     }
     public String getTitle() {
         return title;
     }

}