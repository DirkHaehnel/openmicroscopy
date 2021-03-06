/*
 *   $Id$
 *
 *   Copyright 2007 Glencoe Software, Inc. All rights reserved.
 *   Use is subject to license terms supplied in LICENSE.txt
 */
package ome.server.itests.sec;

import ome.api.IAdmin;
import ome.api.ISession;
import ome.model.meta.Experimenter;
import ome.model.meta.ExperimenterGroup;
import ome.model.meta.Session;
import ome.server.itests.AbstractManagedContextTest;
import ome.system.EventContext;
import ome.system.Principal;

import org.testng.annotations.Test;

/**
 * @author Josh Moore, josh at glencoesoftware.com
 * @since 3.0-Beta2
 */
public class SessionTest extends AbstractManagedContextTest {

    @Test
    public void testSimpleCreate() throws Exception {

        loginRoot();
        Experimenter e = loginNewUser();

        ISession service = this.factory.getServiceByClass(ISession.class);
        Session s = service.createSession(new Principal(e.getOmeName(), "user",
                "Test"), "ome");

        // This is what then gets passed to the
        loginAop.p = new Principal(s.getUuid(), "user", "Test");

        // Now we should be able to do something.
        EventContext ec = this.iAdmin.getEventContext();
        assertEquals(ec.getCurrentUserName(), e.getOmeName());

        service.closeSession(s);
    }

    @Test
    public void testCreationByRoot() throws Exception {
        Experimenter e = loginNewUser();
        loginRoot();

        ISession service = this.factory.getSessionService();
        Principal p = new Principal(e.getOmeName(), "user", "Test");
        Session s = service.createSessionWithTimeout(p, 10 * 1000L);

    }

    @Test(groups = "ticket:1229")
    public void testUpdateDefaultGroup() throws Exception {
        
        ISession s = this.factory.getSessionService();
        IAdmin a = this.factory.getAdminService();

        Experimenter e = loginNewUser();
        ExperimenterGroup g = new ExperimenterGroup(uuid());
        g = new ExperimenterGroup(a.createGroup(g), false);
        
        loginRoot();
        a.addGroups(e, g);
        
        loginUser(e.getOmeName());
        String uuid = a.getEventContext().getCurrentSessionUuid();
        sessionManager.setSecurityContext(new Principal(uuid), g);
        
    }
    
    /**
     * This test original used ISession.updateSession() which is where the bug
     * was. With setSecurityContext() this shouldn't be the case, but leaving
     * test for the moment.
     */
    @Test(groups = "ticket:1385")
    public void testUpdateDefaultGroupTwice() throws Exception {
        
        ISession s = this.factory.getSessionService();
        IAdmin a = this.factory.getAdminService();

        Experimenter e = loginNewUser();
        ExperimenterGroup g1 = new ExperimenterGroup(uuid());
        g1 = new ExperimenterGroup(a.createGroup(g1), false);
        ExperimenterGroup g2 = new ExperimenterGroup(uuid());
        g2 = new ExperimenterGroup(a.createGroup(g2), false);
        
        loginRoot();
        a.addGroups(e, g1, g2);
        
        loginUser(e.getOmeName());
        String uuid = a.getEventContext().getCurrentSessionUuid();
        Principal principal = new Principal(uuid);

        sessionManager.setSecurityContext(principal, g1);
        sessionManager.setSecurityContext(principal, g2);
        
        // But now if we try to get the session again, boom.
        s.getSession(uuid);
    }
}
