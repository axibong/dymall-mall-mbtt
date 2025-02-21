package cn.mbtt.service.mapper;

import cn.mbtt.service.domain.po.PaymentRecords;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PayMapper {

    @Insert("INSERT INTO payment_records(order_id, payment_no, amount, payment_type, status)" +
            "values (#{orderId},#{paymentNo},#{amount},#{paymentType},#{status})")
    int insert(PaymentRecords paymentRecords);
}
