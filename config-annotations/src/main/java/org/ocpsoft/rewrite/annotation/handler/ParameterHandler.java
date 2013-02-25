/*
 * Copyright 2011 <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ocpsoft.rewrite.annotation.handler;

import java.lang.reflect.Field;

import org.ocpsoft.common.util.Assert;
import org.ocpsoft.logging.Logger;
import org.ocpsoft.rewrite.annotation.api.FieldContext;
import org.ocpsoft.rewrite.annotation.api.HandlerChain;
import org.ocpsoft.rewrite.annotation.spi.FieldAnnotationHandler;
import org.ocpsoft.rewrite.bind.Binding;
import org.ocpsoft.rewrite.config.RuleBuilder;
import org.ocpsoft.rewrite.el.El;
import org.ocpsoft.rewrite.param.ParameterBuilder;

public class ParameterHandler extends FieldAnnotationHandler<org.ocpsoft.rewrite.annotation.Parameter>
{

   private final Logger log = Logger.getLogger(ParameterHandler.class);

   @Override
   public Class<org.ocpsoft.rewrite.annotation.Parameter> handles()
   {
      return org.ocpsoft.rewrite.annotation.Parameter.class;
   }

   @Override
   public int priority()
   {
      return HandlerWeights.WEIGHT_TYPE_STRUCTURAL;
   }

   @Override
   public void process(FieldContext context, org.ocpsoft.rewrite.annotation.Parameter annotation, HandlerChain chain)
   {

      // default name is the name of the field
      Field field = context.getJavaField();
      String param = field.getName();

      // but the name specified in the annotation is preferred
      if (!annotation.value().isEmpty()) {
         param = annotation.value().trim();
      }

      if (log.isTraceEnabled()) {
         log.trace("Binding parameter [{}] to field [{}]", param, field);
      }

      // FIXME: We need the ConfigurationRuleBuilder here
      RuleBuilder ruleBuilder = context.getRuleBuilder();
      ParameterBuilder<?> parameterBuilder = ruleBuilder.where(param);

      // subsequent handlers need the builder to configure the parameter
      context.put(ParameterBuilder.class, parameterBuilder);

      // create the binding and publish it in the context
      Binding rawBinding = El.property(field);
      context.put(Binding.class, rawBinding);

      // run subsequent handler to enrich the parameter and wrap the binding
      chain.proceed();

      // add the enriched binding to the parameter
      Binding enrichedBinding = (Binding) context.get(Binding.class);
      Assert.notNull(enrichedBinding, "BindingBuilder was removed from the context");
      parameterBuilder.bindsTo(enrichedBinding);

      // FIXME: hack to trigger ParameterStore attachment
      // parameterBuilder.getRules();

      //
      //
      // // add bindings to conditions by walking over the condition tree
      // AddBindingVisitor visitor = new AddBindingVisitor(context, chain, param, field);
      // context.getRuleBuilder().accept(visitor);
      // if (!visitor.isFound())
      // {
      //
      // /*
      // * Creates a condition that matches the query parameter we are looking for.
      // * It also provides access to a parameter which is named like the parameter that
      // * the AddBindingVisitor is expecting.
      // */
      //
      // // FIXME Commented out until this can be resolved via other means
      // Condition requestParameter = RequestParameter.matches("{name}", "{" + param + "}");
      // // .where("name").matches(param);
      // // FIXME End compilation comment-out
      //
      // /*
      // * Add the query parameter condition to the condition tree. We must make sure that
      // * the condition is evaluated even if the existing part of the tree evaluates to true.
      // */
      // ConditionBuilder composite = context.getRuleBuilder().getConditionBuilder().and(
      // Or.any(requestParameter, True.create()));
      // context.getRuleBuilder().when(composite);
      //
      // /*
      // * This new visitor will definitely find the expected parameter and add the binding.
      // */
      // AddBindingVisitor fallback = new AddBindingVisitor(context, chain, param, field);
      // context.getRuleBuilder().accept(fallback);
      //
      // Assert.assertTrue(fallback.isFound(),
      // "The parameter [" + param + "] bound to Field [" + field.getDeclaringClass().getName() + "."
      // + field.getName() + "] was not found in any condition.");
      //
      // }
      //
      // // continue
      // chain.proceed();

   }

}
