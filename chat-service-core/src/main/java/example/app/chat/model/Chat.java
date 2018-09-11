/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package example.app.chat.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

import org.cp.elements.lang.ObjectUtils;
import org.cp.elements.lang.Renderable;
import org.cp.elements.lang.Renderer;
import org.cp.elements.util.ComparatorResultBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.mapping.annotation.Region;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * The {@link Chat} class is an Abstract Data Type (ADT) modeling a chat in a Instant Messaging (IM) application
 * like Facebook Messenger, Slack, etc.
 *
 * @author John Blum
 * @see java.io.Serializable
 * @see java.lang.Comparable
 * @see java.time.LocalDateTime
 * @see java.util.UUID
 * @see org.cp.elements.lang.Renderable
 * @see org.cp.elements.lang.Renderer
 * @see org.springframework.data.annotation.Id
 * @see org.springframework.data.gemfire.mapping.annotation.Region
 * @see example.app.core.model.Person
 * @since 1.0.0
 */
@Region("Chats")
@RequiredArgsConstructor(staticName = "newChat")
@SuppressWarnings("unused")
public class Chat implements Comparable<Chat>, Renderable, Serializable {

  private static final String CHAT_TO_STRING =
    "{ @type = %1$s, timestamp = %2$s, person = %3$s, message = %4$s }";

  @Getter
  private LocalDateTime timestamp = LocalDateTime.now();

  @Id @Getter
  private String id = UUID.randomUUID().toString();

  @NonNull @Getter
  private final Person person;

  @NonNull @Getter
  private final String message;

  public Chat at(LocalDateTime timestamp) {
    this.timestamp = timestamp != null ? timestamp : LocalDateTime.now();
    return this;
  }

  public Chat identifyBy(String id) {
    this.id = id != null ? id : UUID.randomUUID().toString();
    return this;
  }

  @Override
  @SuppressWarnings("all")
  public int compareTo(Chat chat) {

    return ComparatorResultBuilder.<Comparable>create()
      .doCompare(this.getTimestamp(), chat.getTimestamp())
      .doCompare(this.getPerson(), chat.getPerson())
      .doCompare(this.getMessage(), chat.getMessage())
      .build();
  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }

    if (!(obj instanceof Chat)) {
      return false;
    }

    Chat that = (Chat) obj;

    return ObjectUtils.equals(this.getTimestamp(), that.getTimestamp())
      && ObjectUtils.equals(this.getPerson(), that.getPerson())
      && ObjectUtils.equals(this.getMessage(), that.getMessage());
  }

  @Override
  public int hashCode() {

    int hashValue = 17;

    hashValue = 37 * hashValue + ObjectUtils.hashCode(this.getTimestamp());
    hashValue = 37 * hashValue + ObjectUtils.hashCode(this.getPerson());
    hashValue = 37 * hashValue + ObjectUtils.hashCode(this.getMessage());

    return hashValue;
  }

  @Override
  public String toString() {

    return String.format(CHAT_TO_STRING,
      getClass().getName(), toString(getTimestamp()), getPerson(), getMessage());
  }

  private String toString(LocalDateTime dateTime) {

    return Optional.ofNullable(dateTime)
      .map(it -> it.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")))
      .orElse(null);
  }

  @Override
  @SuppressWarnings("unchecked")
  public String render(Renderer renderer) {
    return renderer.render(this);
  }
}
