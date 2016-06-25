package com.ailk.jdbc.cache;

import com.ailk.jdbc.entity.InfoQuiz;

/**
 * 问题信息值
 * 
 * @author xugq
 * 
 */
public class InfoQuizValue {

	private String title; // 标题
	private short quizType; // 问题类型
	private String options; // 选项列表，以字段分隔符分割
	private String answers; // 答案列表，以字段分隔符分割
	private boolean skip; // 允许第二次答题的时候跳过
	private Integer adId; // 归属的广告ID

	/**
	 * 根据问题信息设置值
	 * 
	 * @param entity
	 *            问题信息
	 */
	public void set(InfoQuiz entity) {
		title = entity.getTitle();
		quizType = entity.getQuizType();
		options = entity.getOptions();
		answers = entity.getAnswers();
		skip = entity.isSkip();
		adId = entity.getAdId();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public short getQuizType() {
		return quizType;
	}

	public void setQuizType(short quizType) {
		this.quizType = quizType;
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
		InfoQuizValue other = (InfoQuizValue) obj;
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

}
