package io.joggr.domain;

import io.joggr.aaa.Roles;
import io.joggr.aaa.SecurityContextProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RepositoryAugmentCurrentUserTest {

    public static final String MOCK_CURRENT_USER_NAME = "mock-current-user-name";
    private RepositoryAugmentCurrentUser testee;
    @Mock
    private Authentication authentication;
    private JogEntry jogEntry;

    @Before
    public void setUp() throws Exception {
        SecurityContextProvider securityContextProvider = mock(SecurityContextProvider.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContextProvider.getContext()).thenReturn(
                securityContext
        );

        when(securityContext.getAuthentication()).thenReturn(
                authentication
        );
        when(authentication.getName()).thenReturn(
                MOCK_CURRENT_USER_NAME
        );

        testee = new RepositoryAugmentCurrentUser(securityContextProvider);
        jogEntry = new JogEntry(1000, 100);
    }

    @Test
    public void testUsernameEnforced() throws Exception {
        when(authentication.getAuthorities()).thenReturn(
                Collections.emptyList()
        );

        testee.applyUserInformationUsingSecurityContext(jogEntry);
        assertEquals(MOCK_CURRENT_USER_NAME, jogEntry.getUserName());
    }

    @Test
    public void testUsernameEnforcedForManagerIfNotProvided() throws Exception {
        when(authentication.getAuthorities()).thenReturn(
                (Collection) Collections.singleton(Roles.ROLE_CONTENT_MANAGER)
        );

        testee.applyUserInformationUsingSecurityContext(jogEntry);
        assertEquals(MOCK_CURRENT_USER_NAME, jogEntry.getUserName());
    }

    @Test
    public void testUsernameNotEnforcedForManager() throws Exception {
        when(authentication.getAuthorities()).thenReturn(
                (Collection) Collections.singleton(Roles.ROLE_CONTENT_MANAGER)
        );
        jogEntry.setUserName("foo");

        testee.applyUserInformationUsingSecurityContext(jogEntry);
        assertEquals("foo", jogEntry.getUserName());
    }

}