package com.abit;

import com.abit.expand.MyLambdaQueryWrapper;
import com.abit.expand.MyPage;
import com.abit.domain.entity.User;
import com.abit.domain.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
public class UserServiceTest extends BaseServiceTest {

	@Autowired
	private IUserService userService;

	@Test
	public void testList() {
		List<User> userList = userService.list(null);
		System.out.println(userList);
	}

	@Test
	public void testAdd() {
		User user = new User();
		user.setDate(LocalDate.now());
		user.setName("name:");
		user.setRemark("remark:");
		user.setStatus((byte)1);
		Assert.assertTrue(userService.save(user));

	}

	@Test
	public void testAddBatch() {
		List<User> entityList = new ArrayList<>();
		IntStream.rangeClosed(1,100).forEach(value -> {
			User user = new User();
			user.setDate(LocalDate.now());
			user.setName("name:" + value);
			user.setRemark("remark:" + value);
			user.setStatus(value % 2 == 0 ? (byte)1 : (byte)2);
			entityList.add(user);
		});
		Assert.assertTrue(userService.saveBatch(entityList));
	}

	@Test
	public void testSelectById() {
		User user = userService.getById(1074915209271197697L,true);
		System.out.println(user);
	}

	@Test
	public void testPage() {
		User user = new User();
		user.setStatus((byte)1);
//		user.setId(1045154144744542209L);
		user.setRemark("remark:");
		QueryWrapper<User> wrapper = new QueryWrapper<>(user);
		Page<User> page = new Page<>(1L,2L);
		IPage<User> userIPage = userService.page(page, wrapper);
		System.out.println(userIPage.getTotal());
		System.out.println(userIPage.getRecords());
	}

	@Test
	public void testPage4CacheByWrapper() {
		//使用 MyLambdaQueryWrapper, 可以实现多种条件查询和分页缓存, 推荐分页缓存都使用该方法

		Integer page = 0;
		Integer size = 100;
		MyLambdaQueryWrapper<User> wrapper = new MyLambdaQueryWrapper<>();
		wrapper.eq(User::getName,"李明").eq(User::getStatus,(byte)1).orderByAsc(User::getId);
		MyPage<User> userIPage = userService.page(wrapper,page,size,true);
		System.out.println(userIPage.getTotal());
		List<User> records = userIPage.getRecords();
		records.forEach(System.out::println);
	}

	@Test
	public void testPage4Cache() {
		//可以实现多种条件查询和分页缓存, 但是基于entity的属性局限性, 不能设置大于/小于/排序等查询条件, 不太推荐使用该方式

		Integer page = 0;
		Integer size = 100;
		User user = new User();
//		user.setRemark("remark:344");
		MyPage<User> userIPage = userService.page(user,page,size,true);
		System.out.println(userIPage.getTotal());
		System.out.println(userIPage.getRecords());
	}

	@Test
	public void testDimensionPage4Cache() {
		//这里只是举例,你可以把User看成Subs订单对象, name:李明 看成 uid:1001, 这里为了获取该用户的订单列表,
		Integer page = 0;
		Integer size = 100;
		User user = new User();
		user.setName("李明");
		MyPage<User> userIPage = userService.page(user,page,size,true,":name:李明");
		System.out.println(userIPage.getTotal());
		System.out.println(userIPage.getRecords());
		// 那么在用户下单后就可以调用  delDimensionPageCache(":uid:1001")方式删除用户id为1001的订单分页缓存, 看下面的 testUpdateDimensionPage4Cache()
	}

	@Test
	public void testUpdateDimensionPage4Cache() {

		User user = new User();
		user.setId(1054663397586935809L);
		user.setName("李海");
		userService.updateById(user);

		userService.delDimensionPageCache(":name:李明");
	}

	@Test
	public void testDel() {
		userService.removeById(1052930181557587969L);
	}

	@Test
	public void testUpdate() {
		User user = new User();
		user.setId(1049927019166924801L);
		user.setRemark("更新" + new Date().toLocaleString());
		userService.updateById(user);

		/*结合业务.看情况是否需要手动删除分页缓存
		subService.delAllPageCache();  或者 subService.delDimensionPageCache(":uid:1001");
		delAllPageCache : 删除整个表的分页缓存,这样对性能影响挺大, 但是这是后台的订单列表才会把采用这种无维度进行分页, 移动端用户每个用户都是只查询自己的订单列表,放到自己的维度缓存,所以即使调用该方法,也不会影响到移动端用户
		delDimensionPageCache : 删除该表某维度的分页缓存, 例如用户维度, 用户下单后需要在订单列表, 马上看到自己的订单,所以在生成订单和支付成功后都需要调用该方法
		*/

	}

    @Test
    public void testTran1(){
        User user = new User();
        user.setName("15007582651");
        user.setRemark("110");
        userService.save(user);
        System.out.println(user.getId());
    }

	@Test
	public void testTran(){
		//参数替换为testTran1()所得的id
		userService.testTran(1074933150498820097L);
	}
}