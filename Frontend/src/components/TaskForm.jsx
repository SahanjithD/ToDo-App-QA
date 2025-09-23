import React, { useState } from 'react'

export default function TaskForm({ onAdd }) {
	const [title, setTitle] = useState('')
	const [description, setDescription] = useState('')
	const [priority, setPriority] = useState('MEDIUM')
	const [submitting, setSubmitting] = useState(false)
	const [titleError, setTitleError] = useState('')

	async function handleSubmit(e) {
		e.preventDefault()
		if (!title.trim()) {
			setTitleError('Title is required')
			return
		}
		setTitleError('')
		setSubmitting(true)
		try {
			await onAdd({ title: title.trim(), description: description.trim(), priority })
			setTitle('')
			setDescription('')
			setPriority('MEDIUM')
		} finally {
			setSubmitting(false)
		}
	}

	return (
		<form className="task-form" onSubmit={handleSubmit} noValidate>
			<div className="row">
				<input
					id="title"
					type="text"
					placeholder="Task title"
					value={title}
					onChange={(e) => setTitle(e.target.value)}
				/>
				<select id="priority" value={priority} onChange={(e) => setPriority(e.target.value)}>
					<option value="HIGH">HIGH</option>
					<option value="MEDIUM">MEDIUM</option>
					<option value="LOW">LOW</option>
				</select>
				<button data-testid="submit-task" type="submit" disabled={submitting}>
					{submitting ? 'Adding...' : 'Add Task'}
				</button>
			</div>
			{titleError && <div className="error-title">{titleError}</div>}
			<textarea
				id="description"
				placeholder="Description (optional)"
				value={description}
				onChange={(e) => setDescription(e.target.value)}
				rows={3}
			/>
		</form>
	)
}

