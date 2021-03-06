/*
 * Copyright 2016 the original author or authors.
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

import static org.cp.elements.lang.RuntimeExceptionsFactory.newIllegalStateException;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.cp.elements.util.ComparatorResultBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.mapping.annotation.Region;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/**
 * The {@link Person} class is an Abstract Data Type (ADT) modeling a person.
 *
 * @author John Blum
 * @see java.io.Serializable
 * @see java.lang.Comparable
 * @see javax.persistence.Entity
 * @see javax.persistence.Table
 * @see org.springframework.data.gemfire.mapping.annotation.Region
 * @since 1.0.0
 */
@Entity
@DiscriminatorColumn(name = "type")
@DiscriminatorValue("person")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "chat_service.people")
@Region("People")
@JsonIgnoreProperties(value = { "age" }, ignoreUnknown = true)
@SuppressWarnings("unused")
public class Person implements Comparable<Person>, Serializable {

	private static final long serialVersionUID = -7204456214709927355L;

	private static final Map<String, Person> cachedPeople = new ConcurrentHashMap<>();

	public static Person newPerson(String firstName, String lastName) {

		Assert.hasText(firstName, "First name is required");
		Assert.hasText(lastName, "Last name is required");

		Person person = new Person();

		person.setFirstName(firstName);
		person.setLastName(lastName);

		return person;
	}

	public static Person newPerson(Person person) {

		Person copy = new Person();

		copy.setBirthDate(person.getBirthDate());
		copy.setFirstName(person.getFirstName());
		copy.setGender(person.getGender());
		copy.setLastName(person.getLastName());

		return copy;
	}

	public static Person newCachedPerson(String firstName, String lastName) {

		return cachedPeople.computeIfAbsent(toFullName(firstName, lastName),
			fullName -> newPerson(firstName, lastName));
	}

	private static String toFullName(String firstName, String lastName) {

		Assert.hasText(firstName, "First name is required");
		Assert.hasText(lastName, "Last name is required");

		return String.format("%1$s %2$s", firstName.trim(), lastName.trim());
	}

	private Long id;

	private LocalDate birthDate;

	private Gender gender;

	private String firstName;
	private String lastName;

	public void setId(Long id) {
		this.id = id;
	}

	@Id
	@javax.persistence.Id
	@GeneratedValue
	public Long getId() {
		return this.id;
	}

	@Transient
	@org.springframework.data.annotation.Transient
	@SuppressWarnings("all")
	public int getAge() {

		LocalDate birthDate = Optional.ofNullable(getBirthDate())
			.orElseThrow(() -> newIllegalStateException("Birth date of person [%s] is unknown", getName()));

		Period period = Period.between(birthDate, LocalDate.now());

		return period.getYears();
	}

	@Transient
	@org.springframework.data.annotation.Transient
	public void setBirthDateFor(int age) {

		Assert.isTrue(age >= 0, "Age must be greater than equal to 0");

		setBirthDate(LocalDate.now().minusYears(age));
	}

	public void setBirthDate(LocalDate birthDate) {

		assertValidBirthDate(birthDate);

		this.birthDate = birthDate;
	}

	protected void assertValidBirthDate(LocalDate birthDate) {

		if (isValidBirthDate(birthDate)) {
			throw new IllegalArgumentException(String.format("[%s] cannot be born after today [%s]",
				getName(), toString(LocalDate.now())));
		}
	}

	protected boolean isValidBirthDate(LocalDate birthDate) {
		return birthDate != null && birthDate.isAfter(LocalDate.now());
	}

	@Column(name = "birth_date")
	public LocalDate getBirthDate() {
		return this.birthDate;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "first_name")
	public String getFirstName() {
		return this.firstName;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	@Enumerated(EnumType.STRING)
	public Gender getGender() {
		return this.gender;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name = "last_name")
	public String getLastName() {
		return this.lastName;
	}

	@Transient
	@org.springframework.data.annotation.Transient
	public String getName() {
		return toFullName(getFirstName(), getLastName());
	}

	@Override
	@SuppressWarnings("unchecked")
	public int compareTo(Person person) {

		return ComparatorResultBuilder.<Comparable>create()
			.doCompare(this.getLastName(), person.getLastName())
			.doCompare(this.getFirstName(), person.getFirstName())
			.doCompare(this.getBirthDate(), person.getBirthDate())
			.build();
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Person)) {
			return false;
		}

		Person that = (Person) obj;

		return ObjectUtils.nullSafeEquals(this.getBirthDate(), that.getBirthDate())
			&& ObjectUtils.nullSafeEquals(this.getFirstName(), that.getFirstName())
			&& ObjectUtils.nullSafeEquals(this.getLastName(), that.getLastName());
	}

	@Override
	public int hashCode() {

		int hashValue = 17;

		hashValue = 37 * hashValue + ObjectUtils.nullSafeHashCode(getBirthDate());
		hashValue = 37 * hashValue + ObjectUtils.nullSafeHashCode(getFirstName());
		hashValue = 37 * hashValue + ObjectUtils.nullSafeHashCode(getLastName());

		return hashValue;
	}

	@Override
	public String toString() {
		return getName();
		//return String.format("{ @type = %1$s, firstName = %2$s, lastName = %3$s, birthDate = %4$s, gender = %5$s }",
		//	getClass().getName(), getFirstName(), getLastName(), toString(getBirthDate()), getGender());
	}

	protected String toString(LocalDate date) {

		return Optional.ofNullable(date)
			.map(it -> it.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
			.orElse(null);
	}

	@SuppressWarnings("unchecked")
	public <T extends Person> T age(int age) {
		setBirthDateFor(age);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public <T extends Person> T as(Gender gender) {
		setGender(gender);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public <T extends Person> T born(LocalDate birthDate) {
		setBirthDate(birthDate);
		return (T) this;
	}
}
