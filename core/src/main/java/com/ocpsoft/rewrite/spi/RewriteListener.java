/*
 * Copyright 2011 Lincoln Baxter, III
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
package com.ocpsoft.rewrite.spi;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.ocpsoft.rewrite.Restricted;
import com.ocpsoft.rewrite.RewriteEvent;

/**
 * Listens to rewrite life-cycle events.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public interface RewriteListener<IN extends ServletRequest, OUT extends ServletResponse> extends Restricted
{
   void onPreWrapRequestCycle(IN request, OUT response);

   void onPreRewrite(RewriteEvent<IN, OUT> event);

   void onPostRewrite(RewriteEvent<IN, OUT> event);

   void onPostChain(RewriteEvent<IN, OUT> event);
}
