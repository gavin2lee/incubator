package base;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
/**
 * 单元测试，需要spring 环境
 * @author chenjinfan
 * @Description
 */

import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.authorization.IUserService;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/config/spring-system.xml"})
@Transactional
@TransactionConfiguration(transactionManager="transactionManager",defaultRollback=false)
@TestExecutionListeners(listeners = {
		DependencyInjectionTestExecutionListener.class,
		TransactionalTestExecutionListener.class })
public class AppUnitTestBase {
	
	protected IUser iUser = null;
	
	@Autowired
	private IUserService iUserService;
	
	@Before
	public void setIUser(){
		iUser = iUserService.getUserById("admin");
	}

}
