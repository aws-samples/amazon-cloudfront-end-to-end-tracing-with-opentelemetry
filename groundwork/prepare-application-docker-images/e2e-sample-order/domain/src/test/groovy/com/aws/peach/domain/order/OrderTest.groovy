import com.aws.peach.domain.order.entity.Orders
import com.aws.peach.domain.order.vo.*
import spock.lang.Specification

class OrderTest extends Specification {

    def "change order state to CLOSED happy case"() {
        given:
        Orders order = Orders.builder()
                .orderState(OrderState.PLACED)
                .build()
        when:
        order.close()
        then:
        order.getOrderState() == OrderState.CLOSED
    }
}