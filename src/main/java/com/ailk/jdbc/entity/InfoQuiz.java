package com.ailk.jdbc.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "info_quiz")
public class InfoQuiz implements Serializable, Cloneable {

	@Id
	@GeneratedValue
	@Column(name = "quiz_id")
	private int quizId;

	@Column(name = "quiz_type", nullable = false)
	private short quizType;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "options")
	private String options;

	@Column(name = "answers")
	private String answers;

	@Column(name = "skip", nullable = false)
	private boolean skip; // 允许第二次答题的时候跳过

	@Column(name = "adId")
	private Integer adId;

	public int getQuizId() {
		return quizId;
	}

	public void setQuizId(int quizId) {
		this.quizId = quizId;
	}

	public short getQuizType() {
		return quizType;
	}

	public void setQuizType(short quizType) {
		this.quizType = quizType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public String getAnswers() {
		return answers;
	}

	public void setAnswers(String answers) {
		this.answers = answers;
	}

	public boolean isSkip() {
		return skip;
	}

	public void setSkip(boolean skip) {
		this.skip = skip;
	}

	public Integer getAdId() {
		return adId;
	}

	public void setAdId(Integer adId) {
		this.adId = adId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((adId == null) ? 0 : adId.hashCode());
		result = prime * result + ((answers == null) ? 0 : answers.hashCode());
		result = prime * result + ((options == null) ? 0 : options.hashCode());
		result = prime * result + quizId;
		result = prime * result + quizType;
		result = prime * result + (skip ? 1231 : 1237);
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InfoQuiz other = (InfoQuiz) obj;
		if (adId == null) {
			if (other.adId != null)
				return false;
		} else if (!adId.equals(other.adId))
			return false;
		if (answers == null) {
			if (other.answers != null)
				return false;
		} else if (!answers.equals(other.answers))
			return false;
		if (options == null) {
			if (other.options != null)
				return false;
		} else if (!options.equals(other.options))
			return false;
		if (quizId != other.quizId)
			return false;
		if (quizType != other.quizType)
			return false;
		if (skip != other.skip)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	@Override
	public InfoQuiz clone() {
		try {
			return (InfoQuiz) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

}
