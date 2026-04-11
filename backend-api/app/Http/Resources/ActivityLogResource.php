<?php

namespace App\Http\Resources;

use Illuminate\Http\Request;
use Illuminate\Http\Resources\Json\JsonResource;

class ActivityLogResource extends JsonResource
{
    /**
     * Transform the resource into an array.
     *
     * @return array<string, mixed>
     */
    public function toArray(Request $request): array
    {
        $targetName = optional($this->targetStudent)->name
            ?? optional($this->targetUser)->name
            ?? 'Unknown';

        return [
            'id'          => $this->id,
            'actor_id'    => $this->actor_id,
            'actor_name'  => optional($this->actor)->name ?? 'System',
            'target_user_id' => $this->target_user_id,
            'target_name' => $targetName,
            'action'      => $this->action,
            'details'     => $this->details,
            'type'        => $this->type,
            'created_at'  => $this->created_at,
        ];
    }
}
