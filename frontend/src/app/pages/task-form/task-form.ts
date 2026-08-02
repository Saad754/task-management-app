import {Component, inject, OnInit, signal} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import {TaskService} from '../../services/task';
import {TaskPriority, TaskStatus} from '../../models/task';

@Component({
  selector: 'app-task-form',
  imports: [FormsModule, RouterLink],
  templateUrl: './task-form.html',
  styleUrl: './task-form.css'
})
export class TaskForm implements OnInit {

  private taskService = inject(TaskService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  taskId = signal<number | null>(null);

  title = signal('');
  description = signal('');
  priority = signal<TaskPriority>('MEDIUM');
  status = signal<TaskStatus>('TODO');
  error = signal('');

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');

    if (id) {
      this.taskId.set(Number(id));

      this.taskService.getTask(Number(id)).subscribe({
        next: (task) => {
          this.title.set(task.title);
          this.description.set(task.description);
          this.priority.set(task.priority);
          this.status.set(task.status);
        },
        error: () => this.error.set('Could not load the task')
      });
    }
  }

  onSave(): void {
    const id = this.taskId();

    if (id) {
      this.taskService.updateTask(id, {
        title: this.title(),
        description: this.description(),
        priority: this.priority(),
        status: this.status()
      }).subscribe({
        next: () => this.router.navigate(['/tasks']),
        error: () => this.error.set('Could not save the task')
      });
    } else {
      this.taskService.createTask({
        title: this.title(),
        description: this.description(),
        priority: this.priority()
      }).subscribe({
        next: () => this.router.navigate(['/tasks']),
        error: () => this.error.set('Could not create the task')
      });
    }
  }
}
