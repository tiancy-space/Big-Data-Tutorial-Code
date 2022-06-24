package demand_analysis

/**
 * @Description: 定义 user_visit_action.txt数据格式对应的样例类,用来对数据做格式转化.
 * @Author: tiancy
 * @Create: 2022/6/24
 */
//用户访问动作表
case class UserVisitAction(
        /**用户点击行为的日期*/
        date: String,
        /**用户的ID*/
        user_id: Long,
        /**具体用户的SessionID*/
        session_id: String,
        /**某个页面的ID (4)*/
        page_id: Long,
        /**动作的时间点*/
        action_time: String,

        /**用户搜索的关键词(6),如果不是搜索行为,则当前行的值为 \N */
        search_keyword: String,

        /**某一个商品品类的ID(7-8:点击行为) 不是点击行为, \N*/
        click_category_id: Long,
        /**某一个商品的ID(8)*/
        click_product_id: Long,

        /**一次订单中所有品类的ID集合(9-10:下单行为) */
        order_category_ids: String,
        /**一次订单中所有商品的ID集合*/
        order_product_ids: String,

        /**一次支付中所有品类的ID集合(11-12:支付行为) */
        pay_category_ids: String,
        /**一次支付中所有商品的ID集合*/
        pay_product_ids: String,

        /**城市 id */
        city_id: Long
)