package com.accenture.pregunton.service;

import com.accenture.pregunton.repository.RulesRepository;
import com.accenture.pregunton.util.ModelUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RulesServiceTest {

  @InjectMocks
  private RulesService rulesService;

  @Mock
  private RulesRepository rulesRepository;

  @Test
  public void save_WhenSendValidData_ShouldReturnARuleWithID() {
    doReturn(ModelUtil.RULES_WITH_ID).when(rulesRepository)
        .save(eq(ModelUtil.RULES));

    rulesService.save(ModelUtil.RULES);
    verify(rulesRepository, times(1)).save(any());
  }

  @Test
  public void save_WhenRulesAreNull_ShouldThrowNullPointerException() {
    assertThrows(NullPointerException.class, () -> rulesService.save(null));
  }
}
