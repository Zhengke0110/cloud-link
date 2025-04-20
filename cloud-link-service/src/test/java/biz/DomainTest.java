package biz;

import com.google.common.hash.Hashing;
import fun.timu.cloud.net.LinkApplication;
import fun.timu.cloud.net.link.manager.DomainManager;
import fun.timu.cloud.net.link.model.DO.Domain;
import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LinkApplication.class)
@Slf4j
public class DomainTest {

    @Autowired
    private DomainManager domainManager;

    @Test
    public void testListDomain() {


        List<Domain> domainDOS = domainManager.listOfficialDomain();

        log.info(domainDOS.toString());

    }


}
