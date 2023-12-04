package com.taixingyiji.gateway_cas_stater.annotate;

import com.taixingyiji.gateway_cas_stater.config.GatewayCasConfig;
import com.taixingyiji.gateway_cas_stater.controller.GatewayController;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lhc
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({GatewayCasConfig.class, GatewayController.class})
public @interface EnableGatewayCas {
}
