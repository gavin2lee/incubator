import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import base.AppUnitTestBase;

import com.harmazing.framework.common.Page;
import com.harmazing.openbridge.paasos.manager.dao.PaaSTenantMapper;
import com.harmazing.openbridge.paasos.manager.model.PaaSTenant;
import com.harmazing.openbridge.paasos.manager.service.IPaaSTenantService;


public class PaasTenentTest extends AppUnitTestBase{
	@Autowired
	private PaaSTenantMapper paasTenantMapper;
	@Autowired
	private IPaaSTenantService paaSTenantService;
	@Test
	public void main(){
		/*PaasTenant tenant = paasTenantMapper.getById("1");
		Assert.assertEquals(tenant.getAdmin().getUserId(), "admin");
		List<PaasTenant> list = paasTenantMapper.list();
		Assert.assertTrue(list.get(0).getMembers().size()==2);*/
		Page<PaaSTenant> page = paaSTenantService.getPage(1, 10);
		Assert.assertNotNull(page);
		List<PaaSTenant> list =  paaSTenantService.getTenantByUserId("j");
		if(list.size()>0){
			Assert.assertTrue(list.get(0).getMember().size()>0);
		}
	}
}
