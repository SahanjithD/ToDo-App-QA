import React from 'react'

export default function TaskRow({ task, onComplete, onDelete }) {
	return (
		<li className={`task-row ${task.completed ? 'completed' : ''}`}>
			<div className="content">
				<div className="title">{task.title}</div>
				{task.description ? <div className="description">{task.description}</div> : null}
			</div>
			<div className="actions">
				<button
					className="complete"
					disabled={task.completed}
					onClick={() => onComplete(task.id)}
				>
					{task.completed ? 'Done' : 'Complete'}
				</button>
				<button className="delete" onClick={() => onDelete(task.id)}>Delete</button>
			</div>
		</li>
	)
}
