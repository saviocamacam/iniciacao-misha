/**
 * Copyright (c) 2010-2017 Mark Allen, Norbert Bartels.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.restfb.types.webhook.messaging;

import com.restfb.Facebook;
import com.restfb.JsonMapper;
import com.restfb.json.JsonObject;
import com.restfb.logging.RestFBLogger;
import com.restfb.types.webhook.messaging.nlp.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NlpResult {

  private List<BaseNlpEntity> convertedEntities = new ArrayList<BaseNlpEntity>();

  @Facebook("entities")
  private JsonObject rawEntities;

  private boolean hasUnknownEntity = false;

  @JsonMapper.JsonMappingCompleted
  public void convertRawEntites(JsonMapper mapper) {
    Iterator<String> keyIterator = rawEntities.keys();
    while (keyIterator.hasNext()) {
      String key = keyIterator.next();
      if ("intend".equals(key)) {
        List<NlpIntend> list = mapper.toJavaList(rawEntities.get(key).toString(), NlpIntend.class);
        convertedEntities.addAll(list);
      } else if ("datetime".equals(key)) {
        List<NlpDatetime> list = mapper.toJavaList(rawEntities.get(key).toString(), NlpDatetime.class);
        convertedEntities.addAll(list);
      } else if ("bye".equals(key)) {
        List<NlpBye> list = mapper.toJavaList(rawEntities.get(key).toString(), NlpBye.class);
        convertedEntities.addAll(list);
      } else if ("reminder".equals(key)) {
        List<NlpReminder> list = mapper.toJavaList(rawEntities.get(key).toString(), NlpReminder.class);
        convertedEntities.addAll(list);
      } else if ("greetings".equals(key)) {
        List<NlpGreetings> list = mapper.toJavaList(rawEntities.get(key).toString(), NlpGreetings.class);
        convertedEntities.addAll(list);
      } else {
        hasUnknownEntity = true;
        RestFBLogger.VALUE_FACTORY_LOGGER
          .warn("Unknown entity found '" + key + "'. Please contact the RestFB team and send this information:\nKey: "
              + key + "\nData: " + rawEntities.get(key).toString());
      }
    }
  }

  /**
   * returns the complete list of all found entities.
   * 
   * @return the complete list of all found entities.
   */
  public List<BaseNlpEntity> getEntities() {
    return convertedEntities;
  }

  /**
   * returns a subset of the found entities.
   *
   * Only entities that are of type <code>T</code> are returned. T needs to extend the {@see BaseNlpEntity}.
   *
   * @param clazz
   *          the filter class
   * @return List of entites, only the filtered elements are returned.
   */
  public <T extends BaseNlpEntity> List<T> getEntities(Class<T> clazz) {
    List<BaseNlpEntity> resultList = new ArrayList<BaseNlpEntity>();
    for (BaseNlpEntity item : getEntities()) {
      if (item.getClass().equals(clazz)) {
        resultList.add(item);
      }
    }
    return (List<T>) resultList;
  }

  /**
   * returns true if there is a entity that cannot be converted by RestFB.
   *
   * Please check your logfile for this and open an issue at Github. We will add the entity then.
   * 
   * @return <code>true</code> if a unknown entity if detected, <code>false</code> otherwise
   */
  public boolean hasUnknownEntity() {
    return hasUnknownEntity;
  }
}
